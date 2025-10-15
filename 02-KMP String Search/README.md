# Knuth-Morris-Pratt (KMP) String Search — KMPsearch

This project implements the **Knuth-Morris-Pratt (KMP)** algorithm for efficient substring searching in plain-text files.  
It searches for a given **target pattern** within a text file line by line and outputs matching lines with the corresponding index positions.

---

## Overview

The **KMP algorithm** improves search efficiency by precomputing a **skip (failure) table**, allowing the search to continue from the most relevant position after a mismatch.  
This prevents redundant character comparisons, making it significantly faster than the naive approach for large texts.

This Java program can:
1. Search a target substring within a text file (e.g., finding `"whale"` in `MobyDick.txt`).
2. Print the **KMP skip table** when run with only a target string argument.

---

## Usage

### Compilation
```bash
javac KMPsearch.java
```

### Run — Search Mode
```bash
java KMPsearch "target" filename.txt
```

**Example**
```bash
java KMPsearch "whale" MobyDick.txt
```

- The program searches each line of `filename.txt` for the substring `"whale"`.
- Matching lines are printed to **standard output**, preceded by the **index (1-based)** of the first occurrence in that line.

---

## Output Format

- Each line containing the target substring is printed **once**, preceded by the index of its **first occurrence**.
- Lines without matches are not printed.

**Example**
```
15: The whale swam through the sea.
98: The giant whale appeared again.
```

---

## Skip Table Mode

When the program is run with only the **target substring**, it prints the **KMP skip table** for that pattern.

**Example**
```bash
java KMPsearch "kokako"
```

**Output**
```
*,k,o,k,a,k,o
a,1,2,3,0,5,6
k,0,1,0,3,0,5
o,1,0,3,2,5,0
*,1,2,3,4,5,6
```

- The first row (`*`) shows the target pattern.  
- The middle rows show skip distances for each character found in the pattern (sorted alphabetically).  
- The final row (`*`) shows skip values for all characters **not** found in the pattern.

---

## Implementation Details

- The KMP **prefix table (skip array)** is constructed based on the target string to determine how far to shift after a mismatch.  
- Each line from the text file is read and searched independently.
- The comparison is **case-sensitive**.
- File reading accounts for newline consistency across different systems.

---

## Example Command Summary

| Mode | Command | Description |
|------|----------|-------------|
| **Compile** | `javac KMPsearch.java` | Compile the program |
| **Search file** | `java KMPsearch "whale" MobyDick.txt` | Search for the word "whale" in the text |
| **Show skip table** | `java KMPsearch "kokako"` | Display the computed KMP skip table |

---

### Note

This project was completed as part of the Bachelor of Computer Science degree at the University of Waikato.  
It is published here solely for educational and portfolio purposes, to showcase my skills in software development.  

All code presented is my own work. Course-specific materials such as assignment descriptions or test data are not included to respect university policies.  

## Academic Integrity
Portfolio-only; not intended for reuse in coursework. Removal on request.
