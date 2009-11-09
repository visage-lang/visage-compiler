/**
 * Regression test: JFXC-3626 : Compiled bind: bound sequence: type-cast
 *
 * Type cast over bound sequence of Integer
 *
 * @test
 * @run
 */

var a = 1.5;
var b = 5.5;
def range = bind [a..b];
def cir = bind range as Integer[];
def cor : Object[] = bind range;
def cor2 = bind range as Object[];
println(cir);
println(cor);
println(cor2);
a = 3.25;
println(cir);
println(cor);
println(cor2);
