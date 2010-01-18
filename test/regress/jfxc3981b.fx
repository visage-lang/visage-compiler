/*
 * Regression test
 * JFXC-3981 : Compiler crash regression in apps/MediaComponent:
 *
 * @test
 * @run
 */

class jfxc3981b {}

public var x: Boolean;

class A {
  public var x: Boolean;
}

class B {
   function f() {
        A { x: bind x with inverse; }
   }
}

function run(){
   var b = B{};
   var f = b.f();
}
