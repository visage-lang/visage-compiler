/**
 * Regression test: JFXC-3619 : Compiled bind: dependent state/mode -- non-bound initalizers of bound object literal
 *
 * Select reference in bound object literal from non-bound initializer (from script-level)
 *
 * @test
 * @run
 */

class A {
  var a : Integer
}


  var ref = A {a: 10 };
  def obla = bind A { a: ref.a }
  
    println(obla.a);
    --ref.a;
    println(obla.a);
    --ref.a;
    println(obla.a);
    --ref.a;
    println(obla.a);
    ref = null;
    println(obla.a);
    ref = A {a: 99 };
    println(obla.a);
