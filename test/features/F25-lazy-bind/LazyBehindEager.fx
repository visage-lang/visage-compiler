/*
 * Feature test for lazy bind: eager binding wrapped around lazy binding (should eliminate laziness)
 *
 * @test
 * @run
 */

var x : Integer = 1
        on replace ov { println("x {ov} -> {x}") };
var y : Integer = bind lazy x + 1
        on replace ov { println("y {ov} -> {y}") };
var z : Integer = bind y + 1
        on replace ov { println("z {ov} -> {z}") };

println("First request");
pz();
println("modify x");
x = 2;
pz();

function pz() { println("reading z"); println("z:{z}") }
