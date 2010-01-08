/*
 * JFXC-3918: TreeView.fx:229: local variable parentView is accessed from within inner class; needs to be declared final
 *
 * @test
 */

class A {
   function func() {};
}

function get(x) {};


function localFunc() {
   var a:A;
   a = A{};
   get(a.func);
}
