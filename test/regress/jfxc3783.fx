/**
 * Regression test JFXC-3783 : compiled-bind: javafx-ui-controls doesn't build anymore
 *
 * @test
 */

class jfxc3783  {
   var outerVar = 5;
   function m() { var a = 1; K { k:bind a}; B{} }
}

class A  {
   var func:function():Void;
}

class K {
   var k = 5;
}

class B extends A {
    var x;
    override var func = function() { x = K{ k: bind outerVar } }
}