/*
 * Regression test JFXC-3902 : Compiled bind: bound explicit sequence out of sync with current invalidation model -- rewrite 
 *
 * check correct initial invalidations
 *
 * @test
 * @run
 */

class A {
  override function toString() { "A" }
}

function gt() : A[] { A{} }

var t = true;
def eb = bind [
		gt(), 
		gt()
	] on replace [a..b] = newV { 
		println(" [{a}..{b}] = {sizeof newV}"); 
	};
println(eb);
