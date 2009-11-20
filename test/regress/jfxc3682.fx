/**
 * JFXC-3682 : Compiled-bind: cannot access enclosing class variable from function value in inner class
 *
 * @test
 */

class jfxc3682 {
   var x:Object;
}

class Inner {
   function f():Void {
      function():Void {x};
   }
}
