/**
 * Regression test JFXC-3990 : Lagging update of bound-if sequence
 *
 * @test
 * @run
 */

var cond = true;
def bc = bind cond on replace { print("cond: {cond} -- size: {sizeof bi} -- content: "); println(bi) };
def bi = bind if (cond) [1..3] else [];
println(bi);
cond = false;
