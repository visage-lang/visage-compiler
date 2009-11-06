/**
 * Regression test: JFXC-3619 : Compiled bind: dependent state/mode -- non-bound initalizers of bound object literal
 *
 * Select reference in bound object literal from non-bound initializer
 *
 * @test
 * @run
 */

class jfxc3619select {
  var a : Integer
}

class Test {
  var ref = this;
  var x = 10;
  def obla = bind jfxc3619select { a: ref.x }
  
  function doit() {
    println(obla.a);
    --x;
    println(obla.a);
    --x;
    println(obla.a);
    --x;
    println(obla.a);
    ref = null;
    println(obla.a);
    ref = this;
    println(obla.a);
  }
}

Test{}.doit()
