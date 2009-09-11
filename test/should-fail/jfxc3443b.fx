/**
 * Regress test for JFXC-3443: attribute invalidate triggers
 *
 * @test/compile-error
 */

class A {
   var x = 1;
   var y = bind x on invalidate { println("Hello! {y}"); } on replace { println("Hello! {y}"); };
}

class B extends A {
   override var y = bind x on invalidate { println("Hello! {y}"); } on replace { println("Hello! {y}"); };
}

var x = 1;
var y = bind x on invalidate { println("Hello! {y}"); } on replace { println("Hello! {y}"); };