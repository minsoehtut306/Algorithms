# 02-strings-regex — Regex → FSM Engine & String Matching

A compact regex-to-FSM engine with a simple stream matcher, built in Java. Includes notes on automata and string-matching concepts (e.g., KMP) alongside a tiny toolchain for compiling a regular expression to an FSM description and searching input text.

---

## Features
- Regex compiler (`REcompile`) that parses a subset of regular expressions and emits an FSM description.
- Stream matcher (`REsearch`) that reads an FSM description from stdin and searches files/stdin for matches.
- Minimal, portable Java (no external deps).
- Notes/specs as text/PDF (problem statements, analysis).

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

---

## Project Layout
```
.
├── REcompile.java   # Regex → FSM compiler
├── REsearch.java    # FSM-driven search over input text
├── simple.txt       # (example input file; optional)
└── docs/            # Specs, notes, analysis (text/PDF)
```

---

## Build
Compile both Java sources:
```bash
javac REcompile.java REresearch.java
```

> Alternatively, compile all sources:
```bash
javac *.java
```

---

## Run

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

### 2) Compile then search a file (via pipe)
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

## Notes
- Text/PDF files in the repo contain problem specs, notes, and/or analysis relevant to the assignment and implementation.
- KMP and related string‑matching ideas may be discussed in notes; the runnable toolchain here is the regex→FSM compiler and searcher.

---

## Academic Integrity & Use
This project was completed as part of the **Bachelor of Computer Science** at the **University of Waikato**. It is published **for educational and portfolio purposes only** to showcase software engineering skills.

- All code is my own work.
- Course‑specific materials (e.g., assignment sheets, official test data) are **not** included to respect university policies.
- **Not intended for reuse in coursework.** Please do not submit this work as part of any academic assessment.
- **Removal on request:** If there are concerns, please open an issue or contact me and I will remove this repository.

---

## License
Personal/portfolio license. You may read and learn from the code. Do not redistribute as coursework or claim ownership.

---

## Quick Status
- Language: Java
- Build: `javac`
- Entry points: `REcompile`, `REresearch`
- Platform: Cross‑platform (JDK 8+ recommended)
