/**
 * Regression test JFXC-3773 : Compiled bind: scoping of bound object literal initializers
 *
 * Sanity
 *
 * @test
 * @run
 */

class jfxc3773base {
  var x : Integer
}

var a = 5;
var b = 3;

var bo = jfxc3773base {
  x: bind a+b
}

println(bo.x);
b = 100;
println(bo.x);
