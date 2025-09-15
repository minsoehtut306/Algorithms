// Min Soe Htut - 1631938
// Last modified: 16 May 2025
import java.util.*;

/**
 * REcompile constructs a finite state machine from a regular expression
 * and outputs it in tabular format suitable for REsearch.
 */
public class REcompile {

    static final int MAX = 1000;        // Max number of FSM states
    static char[] ch = new char[MAX];   // Character for the state (' ', '.', or literal)
    static int[] next1 = new int[MAX];  // First next state
    static int[] next2 = new int[MAX];  // Second next state
    static int state = 1;               // Current available state ID

    static String input;                // Input regex string
    static int pos = 0;                 // Current parse position

    /**
     * Main entry point. Compiles a regex into an FSM and prints it.
     * @param args Command-line arguments: a single regex string
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java REcompile <regexp>");
            return;
        }

        input = args[0];
        pos = 0;
        state = 1;

        Frag f = expression();  // Build FSM fragment from the expression

        int finalTarget;
        if (f.outs.size() > 1) {
            int merge = state++;
            for (int s : f.outs) {
                next1[s] = merge;
                next2[s] = merge;
            }
            finalTarget = state++;
            setState(merge, ' ', finalTarget, finalTarget);
            setState(finalTarget, ' ', -1, -1);
        } else {
            finalTarget = state++;
            for (int s : f.outs) {
                next1[s] = finalTarget;
                next2[s] = finalTarget;
            }
            setState(finalTarget, ' ', -1, -1);
        }

        // Print the FSM
        System.out.printf("0,BR,%d,%d\n", f.start, f.start);
        for (int i = 1; i < state; i++) {
            String label = ch[i] == ' ' ? "BR" : (ch[i] == '.' ? "WC" : String.valueOf(ch[i]));
            System.out.printf("%d,%s,%d,%d\n", i, label, next1[i], next2[i]);
        }
    }

    /**
     * A fragment of an FSM with a start state and a list of unpatched outputs.
     */
    static class Frag {
        int start;
        List<Integer> outs;

        Frag(int start) {
            this.start = start;
            this.outs = new ArrayList<>();
        }
    }

    /**
     * Sets the values for a state in the FSM.
     * @param s The state number.
     * @param c The character label (' ', '.', or literal).
     * @param n1 First next state.
     * @param n2 Second next state.
     */
    static void setState(int s, char c, int n1, int n2) {
        ch[s] = c;
        next1[s] = n1;
        next2[s] = n2;
    }

    /**
     * Parses an expression.
     * Grammar: expression → term ('|' term)*
     * @return FSM fragment representing the parsed expression
     */
    static Frag expression() {
        Frag left = term();
        while (pos < input.length() && input.charAt(pos) == '|') {
            pos++;
            int branch = state++;
            Frag leftSaved = left;
            Frag right = term();
            setState(branch, ' ', leftSaved.start, right.start);
            Frag f = new Frag(branch);
            f.outs.addAll(leftSaved.outs);
            f.outs.addAll(right.outs);
            left = f;
        }
        return left;
    }

    /**
     * Parses a term.
     * Grammar: term - factor+
     * @return FSM fragment representing the sequence
     */
    static Frag term() {
        Frag left = null;
        while (pos < input.length() && input.charAt(pos) != '|' && input.charAt(pos) != ')') {
            Frag f = factor();
            if (left == null) {
                left = f;
            } else {
                for (int s : left.outs) {
                    next1[s] = f.start;
                    next2[s] = f.start;
                }
                left = new Frag(left.start);
                left.outs = f.outs;
            }
        }
        return left != null ? left : new Frag(state++);
    }

    /**
     * Parses a factor and handles *, +, ? operators.
     * Grammar: factor - base [('*' | '+' | '?')]*
     * @return FSM fragment for the factor
     */
    static Frag factor() {
        Frag base = base();

        while (pos < input.length()) {
            char c = input.charAt(pos);

            if (c == '*') {
                pos++;
                int branch = state++;
                int merge = state++;
                setState(branch, ' ', base.start, merge);
                for (int s : base.outs) {
                    next1[s] = branch;
                    next2[s] = branch;
                }
                setState(merge, ' ', 0, 0);
                Frag f = new Frag(branch);
                f.outs.add(merge);
                base = f;

            } else if (c == '+') {
                pos++;
                int branch = state++;
                int merge = state++;
                for (int s : base.outs) {
                    next1[s] = branch;
                    next2[s] = branch;
                }
                setState(branch, ' ', base.start, merge);
                setState(merge, ' ', 0, 0);
                Frag f = new Frag(base.start);
                f.outs = new ArrayList<>();
                f.outs.add(merge);
                base = f;

            } else if (c == '?') {
                pos++;
                int branch = state++;
                int skip = state++;
                setState(branch, ' ', base.start, skip);
                for (int s : base.outs) {
                    next1[s] = skip;
                    next2[s] = skip;
                }
                setState(skip, ' ', 0, 0);
                Frag f = new Frag(branch);
                f.outs.add(skip);
                base = f;

            } else {
                break;
            }
        }

        return base;
    }

    /**
     * Parses a base unit of the regex.
     * Grammar: base - literal | '.' | '\' char | '(' expression ')'
     * @return FSM fragment representing the base
     */
    static Frag base() {
        if (pos >= input.length()) {
            throw new RuntimeException("Unexpected end of input");
        }

        char c = input.charAt(pos);

        if (c == '(') {
            pos++;
            Frag f = expression();
            if (pos >= input.length() || input.charAt(pos) != ')') {
                throw new RuntimeException("Missing closing parenthesis");
            }
            pos++;
            return f;
        }

        if (c == '\\') {
            pos++;
            if (pos >= input.length()) {
                throw new RuntimeException("Invalid escape sequence");
            }
            char escaped = input.charAt(pos++);
            int s = state++;
            setState(s, escaped, 0, 0);
            Frag f = new Frag(s);
            f.outs.add(s);
            return f;
        }

        if (c == '.') {
            pos++;
            int s = state++;
            setState(s, '.', 0, 0);
            Frag f = new Frag(s);
            f.outs.add(s);
            return f;
        }

        pos++;
        int s = state++;
        setState(s, c, 0, 0);
        Frag f = new Frag(s);
        f.outs.add(s);
        return f;
    }
}
