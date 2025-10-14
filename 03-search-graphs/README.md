# Algorithms

- **03-search-graphs** — BFS/DFS, shortest paths, MST, topological ordering

## Features
Supported regex syntax:

```
expression - term ('|' term)*
term       - factor+
factor     - base ('*' | '+' | '?')*
base       - literal | '.' | '(' expression ')' | '\' escaped_char
```

- Literals
- Alternation `|`
- Kleene star `*`
- One-or-more `+`
- Optional `?`
- Grouping `(...)`
- Escapes `\., \|, \(, \), ...`
- Wildcard `.`

> Grammar support reflects the engine used by `REcompile`; see source for edge‑case behavior.


## How to run
Compile both files:
```bash
javac REcompile.java REresearch.java
```

### 1) Compile a regex only
```bash
java REcompile "your_regex_here"
```

**Examples**
```bash
java REcompile z
java REcompile "j|z"
java REcompile "aardvark|zebra"
```

### 2) To run REcompile and REsearch together:
```bash
java REcompile "your_regex_here" | java REresearch input.txt
```
**Examples**
```bash
java REcompile z | java REsearch simple.txt
java REcompile "j|z" | java REsearch simple.txt
java REcompile "aardvark|zebra" | java REsearch simple.txt
```

### 3) Search from stdin
```bash
java REcompile "pattern" | java REsearch
# then type/paste lines and press Ctrl+D (Unix/macOS) or Ctrl+Z Enter (Windows) to end input
```
---

### Note

This project was completed as part of the Bachelor of Computer Science degree at the University of Waikato.  
It is published here solely for educational and portfolio purposes, to showcase my skills in software development.  

All code presented is my own work. Course-specific materials such as assignment descriptions or test data are not included to respect university policies.  

## Academic Integrity
Portfolio-only; not intended for reuse in coursework. Removal on request.