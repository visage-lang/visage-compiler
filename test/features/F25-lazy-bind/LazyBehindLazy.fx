/*
 * Feature test for lazy bind: lazy binding wrapped around lazy binding
 *
 * @test
 * @run
 */

var x : Integer = 1
        on replace ov { println("x {ov} -> {x}") };
var y : Integer = bind lazy x + 1
        on replace ov { println("y {ov} -> {y}") };
var z : Integer = bind lazy y + 1
        on replace ov { println("z {ov} -> {z}") };

println("Starting");
pz();
println("modify x");
x = 2;
pz();

function pz() { println("reading z"); println("z:{z}") }
