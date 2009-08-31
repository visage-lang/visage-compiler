/**
 * JFXC-3370 : Clean up bound..or and bound..and implementation for memory leaks
 *
 * @test
 * @run
 */

// With JFXC-3370, bound..or, bound..and implementation changed. We want to make
// sure things are fine by these sanity checks.

var x : Boolean = true;
var i : Integer = 100;

var y = bind x or (i < 10) on replace oldy {
   println("y changed from {oldy} to {y}");
};

var z = bind lazy x and (i < 10) on replace oldz {
   println("z changed from {oldz} to {z}");
};

x = not x;
println("x changed to {x}");

// now access 'z' to see if lazy change is seen
z;

// change i now
i = 2;
println("i changed to {i}");

// access 'z' to see lazy change
z;

// try bind..or and bind..and as a non-top-level expression
var a = true;
var b = bind not (a or (i < 10));
println("b is {b}");
var c = bind not (a and (i < 10));
println("c is {c}");
