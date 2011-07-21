/**
 * Regression test: JFXC-3578 : Compiled bind: translation of non-transformative bound sequences: identifier, member select, if, ...
 *
 * Bound sequence of identifier
 *
 * @test
 * @run
 */

class jfxc3578cident {
  var q : Object[];
  def bq = bind q;
  
  function doit() {
    println(bq);
    q = ["zip", "zop"];
    println(bq);
    q = [10 .. 100 step 10];
    println(bq);
    q = ["whoop"];
    println(bq);
  }
}

jfxc3578cident{}.doit()

