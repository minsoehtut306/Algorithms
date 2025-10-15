# Heuristic Box Stacking — Simulated Annealing

## Problem Overview

Each box is defined by three integers representing its **dimensions** (height, width, depth).  
Boxes can be rotated into any orientation, giving up to three distinct configurations per box.

A valid stack must satisfy:

- **Touching Faces Condition:**  
  A box can be placed on top of another **only if** its base dimensions are *strictly smaller* (no overhang or flush sides).

- **Single Use Condition:**  
  Each box may appear **at most once** in the final stack (in any valid orientation).

---

## Algorithm Overview

This project implements a **simulated annealing** approach — a probabilistic search technique that iteratively improves a candidate solution.

### Initial Solution Heuristic
A **greedy algorithm** constructs the starting stack:
1. Generate all possible orientations of each box.
2. Sort orientations by descending base area (width × depth).
3. Iteratively add boxes that satisfy stacking constraints and have not been used.

This method increases the likelihood of starting with a feasible and reasonably tall initial stack.

### Annealing Process
1. **Temperature (t):** Controls the number of box modifications (insert, remove, substitute) per iteration.
2. **Cooling Rate (r):** Gradually decreases `t` after each iteration (`t = t - r`).
3. **Acceptance Probability:** Randomly accept worse solutions with decreasing likelihood as temperature cools, helping escape local optima.

---

## How to run

### Compile
```bash
javac NPCStack.java
```

### Run
```bash
java NPCStack <filename> <temperature> <coolingRate>
```

**Parameters**
- `<filename>` — Input file listing box dimensions (e.g., `boxes.txt`)
- `<temperature>` — Initial temperature (integer > 0)
- `<coolingRate>` — Real number where 0.1 ≤ r ≤ t

**Example**
```bash
java NPCStack boxes.txt 100 1.0
```

---

### Output Format

The program prints the final stacked boxes from **top to bottom**, each line showing:

```
width depth height cumulativeStackHeight
```

**Example Output**
```
Improved stack height: 1550
Final stack (top to bottom):
66 69 93 1550
62 66 88 1457
60 65 66 1369
50 63 85 1303
44 61 65 1218
42 57 75 1153
40 54 100 1078
32 52 72 978
29 50 69 906
28 46 97 837
23 42 88 740
22 37 97 652
19 32 71 555
18 31 90 484
17 28 82 394
16 27 58 312
14 22 44 254
4 20 68 210
3 17 97 142
2 5 45 45
```

---

### Note

This project was completed as part of the Bachelor of Computer Science degree at the University of Waikato.  
It is published here solely for educational and portfolio purposes, to showcase my skills in software development.  

All code presented is my own work. Course-specific materials such as assignment descriptions or test data are not included to respect university policies.  

## Academic Integrity
Portfolio-only; not intended for reuse in coursework. Removal on request.
