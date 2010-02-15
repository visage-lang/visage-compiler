/**
 * JFXC-4111 : SDK 282 and higher breakes initialization sequence
 *
 * @test
 * @run
 */

class A {
  var x = 10;
}

class Foo {
  var a = A{};
  var obj = Object{};
  var b = bind a.x on replace { if (obj==null) println("ERROR: obj uninitialized"); }
}

Foo{}
