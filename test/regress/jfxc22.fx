/*
 * jfxc-22; failed to compile with "operator + cannot be applied to java.lang.Object,java.lang.Object"
 * @test
 */

var x;
var y = x;
x = 9;
var z = y + y;

