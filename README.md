# JCalc
A command line scientific calculator written in Java. The calculator converts the entered calculation into Reverse Polish Notation (RPN) using the 'Shunting Yard' algorithm before evaluating the result.

Building JCalc
-------------
JCalc is built into a JAR file using Maven.

Using JCalc
----------
The calcuator supports the standard trigometric functions sin(), cos(), tan(), asin(), acos(), atan() and also sqrt(), log(), ln() and fact() - factorial. The following numerical operators are supported: +, -, *, /, % and ^ (power), plus the following logical operators: &, | and ~ (XOR). The calculator supports decimal, hexadecimal, octal and binary calculations, you can switch modes using the dec, hex, oct and bin commands. Hexadecimal, octal and binary modes only support +, -, /, *, % and logical operators.

The calculator has a self-test mode, just type 'test' once you've entered the calculator in interactive mode. For help, just type 'help' in interactive mode. Debugging output can also be switched on and off with the 'dbgon' and 'dbgoff' commands.

Enjoy!
