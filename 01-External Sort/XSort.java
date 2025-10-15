// MIN SOE HTUT
// Student ID: 1631938

import java.io.*;
import java.util.*;

public class XSort {
   public static void main(String[] args) throws IOException {
       if (args.length < 1 || args.length > 2) {
           System.err.println("Usage: java XSort <runSize> [2]");
           System.exit(1);
       }

       int runSize = Integer.parseInt(args[0]);
       if (runSize < 64 || runSize > 1024) {
           System.err.println("Error: runSize must be between 64 and 1024.");
           System.exit(1);
       }

       if (args.length == 1) {
           createInitialRuns(runSize, null, null);
       } else {
           int k = Integer.parseInt(args[1]);
           if (k != 2) {
               System.err.println("Error: Only balanced 2-way merge for solo Assigmnent.");
               System.exit(1);
           }
           createInitialRuns(runSize, "tape1.txt", "tape2.txt");
           optimizedMerge("tape1.txt", "tape2.txt");
       }
   }

   /**
    * Creates initial runs using Heapsort and writes to temporary files or stdout.
    */
   private static void createInitialRuns(int runSize, String tape1, String tape2) throws IOException {
       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
       List<String> buffer = new ArrayList<>();
       boolean writeToFiles = (tape1 != null && tape2 != null);
       BufferedWriter writer1 = null, writer2 = null;
       if (writeToFiles) {
           writer1 = new BufferedWriter(new FileWriter(tape1));
           writer2 = new BufferedWriter(new FileWriter(tape2));
       }

       int fileToggle = 0;
       String line;
       while ((line = reader.readLine()) != null) {
           buffer.add(line);
           if (buffer.size() == runSize) {
               heapsort(buffer);
               if (writeToFiles) {
                   BufferedWriter writer = (fileToggle % 2 == 0) ? writer1 : writer2;
                   for (String sortedLine : buffer) {
                       writer.write(sortedLine);
                       writer.newLine();
                   }
                   fileToggle++;
               } else {
                   for (String sortedLine : buffer) {
                       System.out.println(sortedLine);
                   }
               }
               buffer.clear();
           }
       }

       if (!buffer.isEmpty()) {
           heapsort(buffer);
           if (writeToFiles) {
               BufferedWriter writer = (fileToggle % 2 == 0) ? writer1 : writer2;
               for (String sortedLine : buffer) {
                   writer.write(sortedLine);
                   writer.newLine();
               }
           } else {
               for (String sortedLine : buffer) {
                   System.out.println(sortedLine);
               }
           }
       }

       if (writeToFiles) {
           writer1.close();
           writer2.close();
       }
   }

   /**
    * Custom heapsort implementation.
    */
   private static void heapsort(List<String> list) {
       int n = list.size();
       for (int i = n / 2 - 1; i >= 0; i--) {
           heapify(list, n, i);
       }

       for (int i = n - 1; i > 0; i--) {
           String temp = list.get(0);
           list.set(0, list.get(i));
           list.set(i, temp);
           heapify(list, i, 0);
       }
   }

   private static void heapify(List<String> list, int n, int i) {
       int largest = i;
       int left = 2 * i + 1;
       int right = 2 * i + 2;

       if (left < n && list.get(left).compareTo(list.get(largest)) > 0) {
           largest = left;
       }

       if (right < n && list.get(right).compareTo(list.get(largest)) > 0) {
           largest = right;
       }

       if (largest != i) {
           String swap = list.get(i);
           list.set(i, list.get(largest));
           list.set(largest, swap);
           heapify(list, n, largest);
       }
   }

   /**
    * Optimized 2-way merge using a PriorityQueue.
    */
   private static void optimizedMerge(String tape1, String tape2) throws IOException {
       BufferedReader reader1 = new BufferedReader(new FileReader(tape1));
       BufferedReader reader2 = new BufferedReader(new FileReader(tape2));
       PriorityQueue<MergeNode> minHeap = new PriorityQueue<>(Comparator.comparing(n -> n.line));

       String line1 = reader1.readLine();
       String line2 = reader2.readLine();

       if (line1 != null) minHeap.add(new MergeNode(line1, reader1));
       if (line2 != null) minHeap.add(new MergeNode(line2, reader2));

       while (!minHeap.isEmpty()) {
           MergeNode node = minHeap.poll();
           System.out.println(node.line);

           String nextLine = node.reader.readLine();
           if (nextLine != null) {
               minHeap.add(new MergeNode(nextLine, node.reader));
           }
       }

       reader1.close();
       reader2.close();
       
       new File("tape1.txt").delete();
       new File("tape2.txt").delete();
   }

   /**
    * Helper class for PriorityQueue Merge.
    */
   private static class MergeNode {
       String line;
       BufferedReader reader;

       public MergeNode(String line, BufferedReader reader) {
           this.line = line;
           this.reader = reader;
       }
   }
}



