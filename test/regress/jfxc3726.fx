/**
 *  jfxc-3726: compiled-bind: missing GETMAP$XXX when object literal is created from mixin
 *
 * @test
 */

class A {
   var x;
   var y;
}

mixin class M {
   function f() {
        A {x: "", y: ""}
   }
}
