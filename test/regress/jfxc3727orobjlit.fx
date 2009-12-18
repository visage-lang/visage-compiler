/**
 * Regression test JFXC-3727 : Compiled bind: bound and/or must cut-off RHS preface
 *
 * Select on an objlit in right-hand-side of or
 *
 * @test
 * @run
 */

class A { 
  var c = true;
  init { println("Oops!") }
}
var b = bind true or A{}.c;
println(b);
