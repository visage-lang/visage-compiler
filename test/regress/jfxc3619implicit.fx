/**
 * Regression test: JFXC-3619 : Compiled bind: dependent state/mode -- non-bound initalizers of bound object literal
 *
 * Implicit select reference to a script-level variable from a bound object literal in a non-bound initalizer
 *
 * @test
 * @run
 */

class A {
  var a : Integer
}

var x = 10;

class jfxc3619implicit {
  def ol = bind A { a: x }
  
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

jfxc3619implicit{}.doit()
