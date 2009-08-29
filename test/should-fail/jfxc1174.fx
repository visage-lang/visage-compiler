/**
 * Regress test for JFXC-1174 - Bi-directional bind with inverse and functions compiles fine but throws runtime exception
 *
 * @test/compile-error
 */

var a = 5;
var b = 10;
var c = bind (a + b);
var d = bind c with inverse;
println("a={a} b={b} c={c} d={d}");
a = 10;
