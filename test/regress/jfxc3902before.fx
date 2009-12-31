/*
 * Regression test JFXC-3902 : Compiled bind: bound explicit sequence out of sync with current invalidation model -- rewrite 
 *
 * simulatanious null out of elements with access to all elements in on-replace.
 * access before version.
 *
 * @test
 * @run
 */

class A {
  override function toString() { "A" }
}

var t = true;
def eb = bind [
		(if (t) A{} else null), 
		(if (t) A{} else null), 
		(if (t) A{} else null), 
		(if (t) A{} else null)
	] on replace [a..b] = newV { 
		for (vv in eb) vv;
		println(" [{a}..{b}] = {sizeof newV}"); 
	};
println(eb);
println(sizeof eb);
t = false;
println(eb);
