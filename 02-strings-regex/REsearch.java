import java.io.*;
import java.util.*;

class State {
    public final String type;
    public final int next1;
    public final int next2;

    public State(String type, int next1, int next2) {
        this.type = type;
        this.next1 = next1;
        this.next2 = next2;
    }
}

class Searcher {
    static String BR = "BR"; // Branch
    static String WC = "WC"; // Wildcard

    private State[] _states;
    private int _finalState;

    public Searcher(State[] states) {
        _states = states;
        _finalState = _states.length-1;
    }

    public int find(String text) {

        int n = text.length();
        var currQ = new ArrayDeque<Integer>();
        var nextQ = new ArrayDeque<Integer>();

        for (int mark = 0; mark <= n; mark++) {
            currQ.clear();
            collectBranchReachable(0, currQ);

            int ptr = mark;
            while (!currQ.isEmpty()) {

                if (currQ.contains(_finalState))
                    // Final state is within reach, meaning we found a match.
                    return mark;

                if (ptr == n) 
                    // No more input, we failed to find a match.
                    break;

                char ch = text.charAt(ptr++);
                nextQ.clear();

                while (!currQ.isEmpty()) {
                    int s = currQ.poll();
                    var state = _states[s];

                    if (state.type.equals(WC) || state.type.equals(String.valueOf(ch))) {
                        collectBranchReachable(state.next1, nextQ);
                        collectBranchReachable(state.next2, nextQ);
                    }
                }

                // Prepare for the next character.
                var tmp = currQ;
                currQ = nextQ;
                nextQ = tmp;
            }
        }

        // Pattern index not found.
        return -1;
    }

    
    private void collectBranchReachable(int root, Deque<Integer> q) {
        if (root == -1) return;

        var visited = new boolean[_states.length];
        var stack = new ArrayDeque<Integer>();
        stack.push(root);

        while (!stack.isEmpty()) {
            int s = stack.pop();
            if (s == -1 || visited[s]) 
                continue;
            visited[s] = true;

            q.add(s);

            var state = _states[s];
            if (state.type.equals(BR)) {
                stack.push(state.next1);
                stack.push(state.next2);
            }
        }
    }
}

class REsearch {
    static String usage = "java REsearch <filename>";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(usage);
            return;
        }

        String filename = args[0];

        try {
            var file = new FileReader(filename);
            var searcher = parseStates(System.in);
            search(searcher, file);
        } catch (IOException ex) {
            System.err.println(ex);
            ex.printStackTrace();
        }

    }

    static void search(Searcher searcher, Reader reader) {
        var scanner = new Scanner(reader);
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            var index = searcher.find(line);
            if (index >= 0)
                System.out.println(line);
        }
        scanner.close();
    }

    public static Searcher parseStates(InputStream input) {
        var scanner = new Scanner(input);
        var states = new ArrayList<State>();

        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            var parts = line.split(",");
            
            if (parts.length == 4) {
                try {
                    int index = Integer.parseInt(parts[0]);
                    String type = parts[1];
                    int next1 = Integer.parseInt(parts[2]);
                    int next2 = Integer.parseInt(parts[3]);

                    states.add(new State(type, next1, next2));

                } catch (NumberFormatException e) {
                    System.err.println("Error parsing integer values from line: " + line);
                }
            } else {
                System.err.println("Invalid line format: " + line);
            }
        }
        
        scanner.close();
        
        return new Searcher(states.toArray(new State[0]));
    }
}