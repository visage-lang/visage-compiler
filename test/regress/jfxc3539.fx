/*
 * Regression test: JFXC-3539 - missing update for mixins and bind with var override.
 *
 * @test
 * @run
 */

mixin class B { 
   var x = 10; 
   var y = 10; 
} 

class C extends B { 
   override var y = bind x; 
} 

println(C{}.y);
