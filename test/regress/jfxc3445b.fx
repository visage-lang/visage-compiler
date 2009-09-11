/*
 * Regress test for JFXC-3445: add translation support for on invalidate triggers
 *
 * @test
 * @run
 */

class Main {
   var x = 1;
   var y = bind lazy x on invalidate { println("Inside on-invalidate..."); }
}

var m = Main{};
println("forcing validation of m.y");
m.y; //force validation of y's contents
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
m.x=2; //this one calls the trigger
println("implicitly invalidating m.y");
m.x=3; //no trigger called - y is already invalid
