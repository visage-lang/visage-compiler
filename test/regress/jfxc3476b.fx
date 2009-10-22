/*
 * Regress test for JFXC-3476: add support for invalidate triggers and invalidate statements for non-bound vars
 *
 * @test
 * @run
 */

class Main {
   var y = 1 on invalidate { println("Inside on-invalidate..."); }
}

var m = Main{};
println("explicitly invalidating m.y");
invalidate m.y; //this one calls the trigger
println("explicitly invalidating m.y");
invalidate m.y; //no trigger called - y is already invalid
println("explicitly invalidating m.y");
invalidate m.y; //no trigger called - y is already invalid
println("forcing validation of m.y");
m.y; //force validation of y's contents
println("explicitly invalidating m.y");
invalidate m.y; //this one calls the trigger
println("explicitly invalidating m.y");
invalidate m.y; //no trigger called - y is already invalid
println("forcing validation of m.y");
m.y; //force validation of y's contents
println("implicitly invalidating m.y");
m.y=2; //this one calls the trigger
println("implicitly invalidating m.y");
m.y=3; //no trigger called - y is already invalid
