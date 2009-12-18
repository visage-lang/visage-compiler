/**
 * Regression test: JFXC-3578 : Compiled bind: translation of non-transformative bound sequences: identifier and member select
 *
 * Sequence member select in a bound object literal
 *
 * @test
 * @run
 */

class jfxc3578objLit {
  var x : String[];
  init {
    println("new instance")
  }
}

var z = jfxc3578objLit { x: ["hi"] }
println("after z");
def bz = bind jfxc3578objLit { x: z.x};
println("after bz");
println("bz.x: '{bz.x}'");
z.x = "sa";
println("after z.x");
println("bz.x: '{bz.x}'");
z = null;
println("after z = null");
println("bz.x: '{bz.x}'");