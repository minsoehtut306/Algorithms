# Regular Expression FSM Compiler & Pattern Search — REcompile & REsearch

This project implements a **regular expression (regexp) compiler** and a **finite state machine (FSM) pattern searcher** written in Java.  
It demonstrates parsing, FSM construction, and text searching using the techniques outlined in lectures for COMPX301 (Pattern Search).

---

## Overview

The project consists of two Java programs:

1. **REcompile.java** — Converts a regular expression into a finite state machine (FSM) description.  
2. **REsearch.java** — Reads the FSM (from standard input) and searches a text file for lines matching the pattern.

Each component can operate independently or together via a Linux command pipeline.

### Workflow

```bash
java REcompile "regex_pattern" | java REresearch textfile.txt
```

- The compiler outputs the FSM as plain text.
- The searcher reads this FSM and prints all lines containing substrings accepted by the machine.

---

## Regular Expression Specification

The implementation supports the following grammar:

```
expression - term ('|' term)*
term       - factor+
factor     - base ('*' | '+' | '?')*
base       - literal | '.' | '(' expression ')' | '\' escaped_char
```

### Supported Operators
| Operator | Meaning |
|-----------|----------|
| **literal** | Matches itself |
| **.** | Wildcard (matches any literal) |
| **\|** | Alternation (logical OR) |
| **\*** | Kleene star (zero or more) |
| **+** | One or more |
| **?** | Optional (zero or one) |
| **(...)** | Grouping for precedence |
| **\\** | Escape character for literals |

**Operator precedence** (highest → lowest):
1. Escaped characters  
2. Parentheses  
3. Repetition / Option (`*`, `+`, `?`)  
4. Concatenation  
5. Alternation (`|`)

> Grammar support reflects the compiler’s FSM generation process as shown in lectures.

---

## How to Run

### 1. Compile Both Files
```bash
javac REcompile.java REresearch.java
```

### 2. Compile a Regular Expression Only
```bash
java REcompile "your_regex_here"
```

**Examples**
```bash
java REcompile z
java REcompile "j|z"
java REcompile "aardvark|zebra"
```

### 3. Run the Compiler and Searcher Together
```bash
java REcompile "your_regex_here" | java REresearch input.txt
```

**Examples**
```bash
java REcompile z | java REresearch simple.txt
java REcompile "j|z" | java REresearch simple.txt
java REcompile "aardvark|zebra" | java REresearch simple.txt
```

### 4. Search Using Standard Input
```bash
java REcompile "pattern" | java REresearch
# Type or paste lines, then press Ctrl+D (Unix/macOS) or Ctrl+Z Enter (Windows)
```

---

## FSM Output Format

Each FSM state is represented as a line with **four comma-separated fields**:

```
state_number, state_type, next_state_1, next_state_2
```

### Example

For the pattern `"a|b"`, the FSM might include states like:
```
0,BR,1,2
1,a,3,3
2,b,3,3
3,END,-1,-1
```

**State Types**
- Literal (e.g., `a`, `b`)
- `BR` (branch)
- `WC` (wildcard)

---

## Example Workflow

**Search lines in `animals.txt` that match `aardvark|zebra`:**

```bash
java REcompile "aardvark|zebra" | java REresearch animals.txt
```

**Expected Output:**
```
Found: zebra in line 14
Found: aardvark in line 52
```

Each matching line is printed **once**, even if multiple substrings match within it.

---

## Implementation Notes

- FSM built using deque-based traversal and branching states.
- Fully self-contained — no external libraries used.
- Designed for Linux (tested in R Block labs).
- Quoted regex patterns (`"pattern"`) ensure safe shell parsing.
- Supports escaped literals (`\.` `\|` `\(` `\)`).

---

## Development Recommendations

- Build and test a parser before FSM generation.
- You can test **REsearch** separately by manually creating FSMs.
- Handle operator edge cases (e.g., `a**`, `a+?`, etc.) gracefully.

---

### Note

This project was completed as part of the Bachelor of Computer Science degree at the University of Waikato.  
It is published here solely for educational and portfolio purposes, to showcase my skills in software development.  

All code presented is my own work. Course-specific materials such as assignment descriptions or test data are not included to respect university policies.  

## Academic Integrity
Portfolio-only; not intended for reuse in coursework. Removal on request.
