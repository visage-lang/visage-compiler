/* 
 * JFXC-3553 : Compiled-bind: JavafxToJava crashes on postfix operator with instance variable.
 *
 * @test
 * @run
 */

class A {
  var x = 1;
}

// A{}.x++; // this worked
var a = A {};
a.x++; //used to crash the compiler
// print it for expected result file
println(a.x);
