/*
 * Feature test for lazy bind: binding to sequences
 *
 * @test
 * @run
 */

// Simple bind

var x = [ 1, 2, 3 ]
    on replace oldX[a..b]=newElements { println("x[{a}..{b}] => [ { newElements } ]" };
var y = bind lazy x
    on replace oldY[a..b]=newElements { println("y[{a}..{b}] => [ { newElements } ]" };

println("Simple bind");
py();
println("modify x");
insert 4 into x;
py();

function py() { println("reading y"); println("y:{y}") }

// Bind to sizeof

var sizex = bind lazy sizeof x
    on replace = ov { println("sizeof x {ov} => {x}" };

// Bind to element

// Bind to member

// Bind to if

// Bind to reverse

// Bind to range

// Bind to concatenate

// Bind to singleton

// Bind to comprehension

