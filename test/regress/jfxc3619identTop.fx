/**
 * Regression test: JFXC-3619 : Compiled bind: dependent state/mode -- non-bound initalizers of bound object literal
 *
 * Identifier reference to same level (instance) var in bound object literal (of top) from non-bound initializer
 *
 * @test
 * @run
 */

class jfxc3619identTop {
  var a : Integer
}

class Test {
  var x = 10;
  def ol = bind jfxc3619identTop { a: x }
  
  function doit() {
    println(ol.a);
    --x;
    println(ol.a);
    --x;
    println(ol.a);
    --x;
    println(ol.a);
  }
}

Test{}.doit()
