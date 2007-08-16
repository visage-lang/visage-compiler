// Fails to compile with "operator + cannot be applied to java.lang.Object,java.lang.Object"
// Issue 22 "Types not determined transitively"
var x;
var y = x;
x = 9;
var z = y + y;

