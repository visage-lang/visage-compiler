/**
 * JFXC-3690 : Compiled-bind: cannot access function in enclosing type
 *
 * @test
 */

class A {
   function test():Void {
      f;
   }
}

class jfxc3690 {
   function f() {}
}
