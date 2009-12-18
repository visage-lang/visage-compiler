/*
 * Regression :  JFXC-3799 - compiled-bind: backend error if mixee class declaration precedes non-empty mixin class declaration
 *
 * @test
 */

mixin class B extends A {
}

mixin class A {
  var s:String;
}

class D extends C, B {}

class C extends A {}
