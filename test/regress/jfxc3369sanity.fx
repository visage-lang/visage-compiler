/**
 * JFXC-3369 : Fix leaks caused by bound..if implementation.
 *
 * @test
 * @run
 */

// With JFXC-3369, bound..if implementation changed. We want to make
// sure things are fine by these sanity checks.

var x : Boolean = true;

println("x is {x}");

var y = bind if (x) "hello" else "world" on replace oldy {
   println("y changed from {oldy} to {y}");
};

var z = bind lazy if (x) "hello" else "world" on replace oldz {
   println("z changed from {oldz} to {z}");
};

x = not x;
println("x changed to {x}");

// now access 'z' to see if lazy change is seen
z;

// try bind..if as a non-top-level expression
var a = true;
var b = bind (if (a) "hello" else "world").toUpperCase();
println("b is {b}");
a = not a;
println("new b is {b}");

