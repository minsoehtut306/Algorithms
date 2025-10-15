// Min Soe Htut
// Student ID: 1631938

import java.util.*;
import java.io.*;

public class KMPsearch {

    public static void main(String[] args) {
        // Handles command-line input: one arg = skip table, two args = file search
        if (args.length == 1) {
            String pattern = args[0];
            Map<Character, int[]> skipTable = buildSkipTable(pattern);
            printSkipTable(pattern, skipTable);
        } else if (args.length == 2) {
            String pattern = args[0];
            String filename = args[1];
            Map<Character, int[]> skipTable = buildSkipTable(pattern);
            searchInFile(filename, pattern, skipTable);
        } else {
            System.err.println("Usage:");
            System.err.println("  java KMPsearch \"pattern\"            (to print skip table)");
            System.err.println("  java KMPsearch \"pattern\" file.txt   (to search file)");
        }
    }

    // Builds KMP skip table where each row corresponds to a character and each column to pattern position
    private static Map<Character, int[]> buildSkipTable(String pattern) {
        int m = pattern.length();
        Set<Character> alphabet = new TreeSet<>();
        for (char c : pattern.toCharArray()) {
            alphabet.add(c);
        }

        Map<Character, int[]> skipTable = new LinkedHashMap<>();

        for (char ch : alphabet) {
            int[] row = new int[m];
            for (int i = 0; i < m; i++) {
                int skip = 0;
                for (int k = i + 1; k >= 0; k--) {
                    if (k == 0) {
                        skip = i + 1;
                        break;
                    }
                    boolean match = true;
                    for (int j = 0; j < k - 1; j++) {
                        if (pattern.charAt(j) != pattern.charAt(i - k + 1 + j)) {
                            match = false;
                            break;
                        }
                    }
                    int suffixEnd = i - k + 1 + (k - 1);
                    if (match && suffixEnd >= 0 && pattern.charAt(k - 1) == ch) {
                        skip = i - k + 1;
                        break;
                    }
                }
                row[i] = skip;
            }
            skipTable.put(ch, row);
        }

        // Row for non-pattern characters
        int[] nonPatternRow = new int[m];
        for (int i = 0; i < m; i++) {
            nonPatternRow[i] = i + 1;
        }
        skipTable.put('*', nonPatternRow);

        return skipTable;
    }

    // Prints the skip table 
    private static void printSkipTable(String pattern, Map<Character, int[]> table) {
        // First row: the pattern characters
        System.out.print("*,");
        for (int i = 0; i < pattern.length(); i++) {
            System.out.print(pattern.charAt(i));
            if (i < pattern.length() - 1) System.out.print(",");
        }
        System.out.println();

        // Alphabetical order for character rows
        for (Map.Entry<Character, int[]> entry : table.entrySet()) {
            char ch = entry.getKey();
            if (ch == '*') continue;
            System.out.print(ch);
            for (int val : entry.getValue()) {
                System.out.print("," + val);
            }
            System.out.println();
        }

        // Print the non-pattern row last 
        System.out.print("*");
        for (int val : table.get('*')) {
            System.out.print("," + val);
        }
        System.out.println();
    }

    // KMP string search using skip table
    private static void searchInFile(String filename, String pattern, Map<Character, int[]> skipTable) {
        int m = pattern.length();

        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int i = 0, j = 0;

                while (i < line.length()) {
                    if (line.charAt(i) == pattern.charAt(j)) {
                        i++;
                        j++;
                        if (j == m) {
                            // Output index is 1-based
                            System.out.println((i - m + 1) + " " + line);
                            break;
                        }
                    } else {
                        char mismatchChar = line.charAt(i);
                        int[] skips = skipTable.getOrDefault(mismatchChar, skipTable.get('*'));
                        int skip = (j < skips.length) ? skips[j] : 1;
                        i += skip;
                        j = 0;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
