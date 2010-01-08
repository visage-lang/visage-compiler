/*
 * Regression test JFXC-3902 : Compiled bind: bound explicit sequence out of sync with current invalidation model -- rewrite 
 *
 * null stuff out
 *
 * @test
 * @run
 */

class A {
  override function toString() { "A" }
}

var a = A{};
var t = true;
var s = [A{}, A{}, A{}];
var z = A{};
def eb = bind [
		a, 
		(if (t) A{} else null), 
		s, 
		z
	] on replace [a..b] = newV { 
		println(" [{a}..{b}] = {sizeof newV}"); 
		for (vv in eb) vv;
	};
z = null;
delete s[1];
s = null;
t = false;
a = null;
println(eb);
