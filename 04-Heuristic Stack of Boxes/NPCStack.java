import java.io.*;
import java.util.*;

/**
 * Name: Min Soe Htut
 * Id: 1631938
 * Date: 05/06/2025
 *
 * Initial Solution Heuristic:
 * A greedy algorithm is used to construct the initial stack. All box orientations
 * are sorted by descending base area (width × depth), then iteratively added if they
 * obey the stacking constraints and have not been used before. This increases the
 * chance of placing smaller boxes on top of large ones.
 *
 * Neighbourhood Search Strategy:
 * 1. REMOVE – remove a random box
 * 2. INSERT – insert an unused box at a random position
 * 3. SWAP – swap two boxes in the stack
 * 4. ROTATE – replace a box with a different orientation (same ID)
 * 5. SHIFT – move a box one position up/down
 *
 * Simulated annealing probabilistically accepts worse solutions to escape local optima.
 * The best valid solution found during the process is returned.
 */
public class NPCStack {
    static Random rng;

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java NPCStack <filename> <temperature> <coolingRate> [stopThreshold]");
            return;
        }

        String filename = args[0];
        double temperature = Double.parseDouble(args[1]);
        double coolingRate = Double.parseDouble(args[2]);
        double stopThreshold = args.length >= 4 ? Double.parseDouble(args[3]) : 0.5;

        rng = new Random();

        List<Box> allBoxes = readBoxesFromFile(filename);
        if (allBoxes == null || allBoxes.isEmpty()) {
            System.out.println("No valid boxes loaded.");
            return;
        }

        List<Box> currentStack = generateInitialSolution(allBoxes);
        int currentHeight = stackHeight(currentStack);

        int bestHeight = currentHeight;
        List<Box> bestStack = new ArrayList<>(currentStack);

        double t = temperature;
        while (t > stopThreshold) {
            List<Box> newStack = generateNeighbour(currentStack, allBoxes);
            int newHeight = stackHeight(newStack);
            double acceptProb = Math.exp((newHeight - currentHeight) / t);

            if (newHeight > currentHeight || rng.nextDouble() < acceptProb) {
                currentStack = newStack;
                currentHeight = newHeight;
                if (newHeight > bestHeight) {
                    bestHeight = newHeight;
                    bestStack = new ArrayList<>(newStack);
                }
            }
            t -= coolingRate;
        }

        System.out.println("Improved stack height: " + bestHeight);
        System.out.println("Final stack (top to bottom):");
        printStack(bestStack);
    }

    /**
     * Reads input boxes from a file and generates all 3 rotations of each box.
     */
    static List<Box> readBoxesFromFile(String filename) {
        List<Box> allBoxes = new ArrayList<>();
        int id = 0;
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] tokens = line.split("\\s+");
                if (tokens.length != 3) continue;

                try {
                    int a = Integer.parseInt(tokens[0]);
                    int b = Integer.parseInt(tokens[1]);
                    int c = Integer.parseInt(tokens[2]);
                    if (a <= 0 || b <= 0 || c <= 0) continue;

                    allBoxes.add(new Box(a, b, c, id));
                    allBoxes.add(new Box(b, c, a, id));
                    allBoxes.add(new Box(c, a, b, id));
                    id++;
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        }
        return allBoxes;
    }

    /**
     * Constructs an initial greedy stack by sorting boxes by descending base area
     * and selecting the largest fitting box at each step.
     */
    static List<Box> generateInitialSolution(List<Box> allBoxes) {
        List<Box> sorted = new ArrayList<>(allBoxes);
        sorted.sort((b1, b2) -> Integer.compare(b2.width * b2.depth, b1.width * b1.depth));
        List<Box> stack = new ArrayList<>();
        Set<Integer> used = new HashSet<>();

        for (Box box : sorted) {
            if (used.contains(box.id)) continue;
            if (stack.isEmpty() || canStack(box, stack.get(stack.size() - 1))) {
                stack.add(box);
                used.add(box.id);
            }
        }
        return stack;
    }

    /**
     * Applies a random mutation (insert, remove, swap, rotate, shift) to generate
     * a neighboring solution. Repairs the stack to maintain validity.
     */
    static List<Box> generateNeighbour(List<Box> current, List<Box> pool) {
        List<Box> newStack = new ArrayList<>(current);
        Set<Integer> usedIds = new HashSet<>();
        for (Box b : newStack) usedIds.add(b.id);

        int action = rng.nextInt(5);

        switch (action) {
            case 0:
                if (!newStack.isEmpty()) {
                    int idx = rng.nextInt(newStack.size());
                    newStack.remove(idx);
                }
                break;
            case 1:
                for (int i = 0; i < 10; i++) {
                    Box candidate = pool.get(rng.nextInt(pool.size()));
                    if (!usedIds.contains(candidate.id)) {
                        int pos = rng.nextInt(newStack.size() + 1);
                        newStack.add(pos, candidate);
                        break;
                    }
                }
                break;
            case 2:
                if (newStack.size() > 1) {
                    int i = rng.nextInt(newStack.size());
                    int j = rng.nextInt(newStack.size());
                    Collections.swap(newStack, i, j);
                }
                break;
            case 3:
                if (!newStack.isEmpty()) {
                    int idx = rng.nextInt(newStack.size());
                    Box old = newStack.get(idx);
                    for (Box b : pool) {
                        if (b.id == old.id && !b.equals(old)) {
                            newStack.set(idx, b);
                            break;
                        }
                    }
                }
                break;
            case 4:
                if (newStack.size() > 1) {
                    int from = rng.nextInt(newStack.size());
                    int to = from + (rng.nextBoolean() ? 1 : -1);
                    if (to >= 0 && to < newStack.size()) {
                        Collections.swap(newStack, from, to);
                    }
                }
                break;
        }

        // Repair the stack
        List<Box> repaired = new ArrayList<>();
        Set<Integer> added = new HashSet<>();
        for (Box b : newStack) {
            if (!added.contains(b.id) &&
                (repaired.isEmpty() || canStack(b, repaired.get(repaired.size() - 1)))) {
                repaired.add(b);
                added.add(b.id);
            }
        }
        return repaired;
    }

    /**
     * Returns true if 'top' can be stacked on 'below' (i.e., both width and depth are smaller).
     */
    static boolean canStack(Box top, Box below) {
        return top.width < below.width && top.depth < below.depth;
    }

    /**
     * Calculates the total height of a stack of boxes.
     */
    static int stackHeight(List<Box> stack) {
        return stack.stream().mapToInt(b -> b.height).sum();
    }

    /**
     * Prints the stack from top to bottom along with cumulative height remaining.
     */
    static void printStack(List<Box> stack) {
        int totalHeight = stackHeight(stack);
        for (Box box : stack) {
            System.out.printf("%d %d %d %d%n", box.width, box.depth, box.height, totalHeight);
            totalHeight -= box.height;
        }
    }

    /**
     * A box is defined by its (sorted) width, depth, height, and unique ID.
     */
    static class Box {
        int width, depth, height, id;

        Box(int w, int d, int h, int id) {
            int[] dims = {w, d, h};
            Arrays.sort(dims);
            this.width = dims[0];
            this.depth = dims[1];
            this.height = dims[2];
            this.id = id;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Box)) return false;
            Box b = (Box) o;
            return b.width == width && b.depth == depth && b.height == height && b.id == id;
        }
    }
}
