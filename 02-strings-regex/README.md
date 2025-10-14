# Algorithms

- **02-strings-regex** — regex → FSM engine, KMP/string matching, automata notes

## How to run
Compile both files:
```bash
javac KMPsearch.java
java KMPsearch "target" filename.txt
```
### Example:

An example of usage for the program with just a target substring is as follows:

   % java KMPsearch "kokako"

which computes the skip array for "kokako" and prints it as the following five lines to standard output:

*,k,o,k,a,k,o

a,1,2,3,0,5,6

k,0,1,0,3,0,5

o,1,0,3,2,5,0

*,1,2,3,4,5,6

### Note

This project was completed as part of the Bachelor of Computer Science degree at the University of Waikato.  
It is published here solely for educational and portfolio purposes, to showcase my skills in software development.  

All code presented is my own work. Course-specific materials such as assignment descriptions or test data are not included to respect university policies.  

## Academic Integrity
Portfolio-only; not intended for reuse in coursework. Removal on request.