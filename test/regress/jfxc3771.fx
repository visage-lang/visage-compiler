/**
 * JFXC-3771 : Compiled bind: Cannot find symbol: class javafx on rect.fill => Color.RED
 *
 * @test
 */

class A {
   var f:function():Void;
}

class C {
    var x:Object ;
    var a:A = A{
        f: function ():Void {
             x => null;
        }
    };
}
