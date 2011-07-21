/**
 * Regress test for JFXC-4230: Mixin function chosen instead of super function
 *
 * @test
 * @run
 */

class Base {
   public function func():Void {
       println("Base::func called");
   }
}

mixin class Mixin {
   public function func():Void {
       println("Mixin::func called");
   }
}

class Sub extends Base, Mixin {
}

def s = Sub { }
s.func();
