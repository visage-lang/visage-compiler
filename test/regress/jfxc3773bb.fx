/**
 * Regression test JFXC-3773 : Compiled bind: scoping of bound object literal initializers
 *
 * Bound context, bound initializer.
 *
 * @test
 * @run
 */

class jfxc3773bb {
  var x : Integer
}

var a = 5;
var b = 3;

var bo = bind jfxc3773bb {
  x: bind a+b
}

println(bo.x);
b = 100;
println(bo.x);
