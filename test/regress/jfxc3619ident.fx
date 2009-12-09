/**
 * Regression test: JFXC-3619 : Compiled bind: dependent state/mode -- non-bound initalizers of bound object literal
 *
 * Identifier reference to same level (instance) var in bound object literal from non-bound initializer
 *
 * @test
 * @run
 */

class A {
  var a : Integer
}

class jfxc3619ident {
  var x = 10;
  def ol = bind A { a: x }

  function doit() {
    println(ol.a);
    def hash1 = java.lang.System.identityHashCode(ol);
    --x;
    if (hash1 == java.lang.System.identityHashCode(ol)) println("Error: No new object for --x");
    println(ol.a);
    --x;
    println(ol.a);
    --x;
    println(ol.a);
  }
}

jfxc3619ident{}.doit()
