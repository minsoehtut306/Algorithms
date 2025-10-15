# External Balanced Sort Merge — XSort

This project implements an **external sorting algorithm** designed to handle large text files that cannot fit entirely into memory.  
The implementation follows the **Balanced k-way Sort Merge** approach using **Heapsort** for initial run generation.

---

## Algorithms Used

### 1. Heapsort for Initial Run Generation
- Input text is divided into **runs** of fixed size (64–1024 lines).
- Each run is sorted in memory using **Heapsort**, implemented from scratch.
- Sorted runs are written sequentially to standard output, forming the initial run file.

### 2. Balanced k-way Sort Merge
- The algorithm performs **balanced merging passes** using temporary files (e.g., `tape1`, `tape2`, etc.).
- In each pass:
  - Runs are distributed across multiple temporary files.
  - A **k-way merge** (k = 2 or 4) reads from the temporary input files and writes merged results to temporary output files.
  - The process repeats until a single sorted run remains.
- Supports:
  - **2-way merge** (solo submission)
  - **4-way merge** (pair submission)

This technique ensures efficient sorting for data sets larger than main memory, similar to how the Linux `sort` command works.

---

## How to Run

### Compile
```bash
javac XSort.java
```

### Create Initial Runs
```bash
cat MobyDick.txt | java XSort 512 > runs.txt
```

### Perform Balanced Merge
```bash
cat MobyDick.txt | java XSort 64 2 > Moby.sorted
```

### Notes
- The first argument specifies the **run length** (64–1024).  
- The optional second argument (`2` or `4`) specifies the **merge type**.
- Temporary files (e.g., `tape1`, `tape2`, …) are used during merging and are automatically managed by the program.

---

## Input and Output

### Input
- Plain text data from **standard input**.  
- Each line is treated as a single record and compared lexicographically using Java’s `String.compareTo()`.

### Output
- Sorted text written to **standard output**.  
- Ensures that the size of the sorted output matches the input (no lost newline characters).

---

## Example Commands

**Generate runs only:**
```bash
cat MobyDick.txt | java XSort 512 > runs.txt
```

**Perform 2-way merge:**
```bash
cat MobyDick.txt | java XSort 64 2 > Moby.sorted
```

**Perform 4-way merge (if implemented):**
```bash
cat MobyDick.txt | java XSort 64 4 > Moby.sorted
```

---

## Implementation Notes

- Handles newline consistency across operating systems (see:  
  [StackOverflow: Keep newlines when reading a file](https://stackoverflow.com/questions/42769110/keep-new-lines-when-reading-in-a-file)).
- Designed for Linux command-line operation with pipes (`|`) and redirections (`>`).
- Works with large text files efficiently by minimizing memory use.

---

### Note

This project was completed as part of the Bachelor of Computer Science degree at the University of Waikato.  
It is published here solely for educational and portfolio purposes, to showcase my skills in software development.  

All code presented is my own work. Course-specific materials such as assignment descriptions or test data are not included to respect university policies.  

## Academic Integrity
Portfolio-only; not intended for reuse in coursework. Removal on request.

