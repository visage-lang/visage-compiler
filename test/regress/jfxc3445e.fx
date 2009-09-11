/*
 * Regress test for JFXC-3445: add translation support for on invalidate triggers
 *
 * @test
 * @run
 */

class Main {
  var x;
  function f() {};
}

Main{
override function f():Void {
   var x = 1;
   var local = bind x on invalidate {println("Inside on-invalidate trigger: x is {x}");}
   println("forcing validation of local var");
   local;
   println("explicitly invalidating local var");
   invalidate local;
   println("forcing validation of local var");
   local;
   println("implicitly invalidating local var");
   x = 2;
}
}.f();
