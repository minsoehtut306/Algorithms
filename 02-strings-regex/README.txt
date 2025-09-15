Min Soe Htut : 1631938 REcompile
Robert Howie : 1630895REsearch
Last modified: 16 May 2025  

USAGE
To compile both files:
  javac REcompile.java REsearch.java

To run REcompile only:
  java REcompile "your_regex_here"

Example:
  java REcompile z
  java REcompile "j|z"
  java REcompile "aardvark|zebra"

To run REcompile and REsearch together:
  java REcompile "your_regex_here" | java REsearch input.txt

Example:
  java REcompile z | java REsearch simple.txt
  java REcompile "j|z" | java REsearch simple.txt
  java REcompile "aardvark|zebra" | java REsearch simple.txt

GRAMMAR RULES USED FOR REcompile:

expression - term ('|' term)*  
term       - factor+  
factor     - base ('*' | '+' | '?')*  
base       - literal | '.' | '(' expression ')' | '\' escaped_char

Supports:
- Literal characters
- Alternation (|)
- Kleene star (*)
- One-or-more (+)
- Optional (?)
- Grouping with parentheses ()
- Escaped characters (e.g., \., \|, \(, \), etc.)
- Wildcard (.)