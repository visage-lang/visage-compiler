/*
 * Regression test 
 * JFXC-3881 : Compiled bind optimization: sequence element access through elem$, not Sequence.get
 *
 * Access in a return
 *
 * @test
 * @run
 */

class A {
  def bf = bind for (i in [0..4]) i
}
var a = A{}

function f() {
  a.bf[3]
}
println(f())
