/*
 * Regress test for JFXC-3445: add translation support for on invalidate triggers
 *
 * @test
 * @run/fail
 */

class Base {
   var x = 1;
   var  y = 1;
}

class Main extends Base {
   override var y = x + 5;
}

var m = Main{};
println("forcing validation of m.y");
m.y; //force validation of y's contents
println("explicitly invalidating m.y");
invalidate m.y; //this one calls the trigger (runtime error as m.y is not bound)
