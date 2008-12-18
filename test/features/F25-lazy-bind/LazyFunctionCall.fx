/*
 * Feature test for lazy bind: function call
 *
 * @test
 * @run
 */

var x : Integer = 1
        on replace ov { println("x {ov} -> {x}") };
var y : Integer = 2
        on replace ov { println("y {ov} -> {y}") };

function plus(a:Integer, b:Integer) { a + b }

var z : Integer = bind lazy plus(x, y)
        on replace ov { println("z {ov} -> {z}") };

println("==lazy function call");
pz();
pz();
println("modify x");
x = 9;
pz();
println("modify y");
y = 3;
pz();

function pz() { println("reading z"); println("z:{z}") }