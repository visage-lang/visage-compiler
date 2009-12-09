/**
 * Regression test: JFXC-3626 : Compiled bind: bound sequence: type-cast
 *
 * Bound type cast over sequence -- on-invalidate
 *
 * @test
 * @run
 */

var a = 1.5;
var b = 5.5;
def range = bind [a..b];
def cor : Object[] = bind range on invalidate { println("ruff") }
a = 3.25;
b = 7.5;
println("---");
println(cor);
a = 1.6;
b = 4.7;
