/*
 * Feature test for lazy bind: simple binding
 *
 * @test
 * @run
 */

var x : Integer = 1
        on replace ov { println("x {ov} -> {x}") };
var y : Integer = bind lazy x + 1
        on replace ov { println("y {ov} -> {y}") };

var z : Integer = 2;
var b = bind lazy (y == z)
        on replace ov { println("b {ov} -> {b}") };

println("Starting");
py();
py();
pb();
pb();
println("modify x");
x = 2;
py();
pb();

println("modify z");
z = 3;
pb();

function py() { println("reading y"); println("y:{y}") }
function pb() { println("reading b"); println("b:{b}") }
