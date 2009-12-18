/**
 * Regression test: JFXC-3670 : Compiled bind: bind permeates object literal initializers
 *
 * @test
 * @run
 */

class Baffle {
  var moe : Integer;
  init { println("Baffle:{moe}") }
}
var x = 2;
function foo(k : Integer) { println("foo({k})"); k }
var zzz = bind Baffle { moe: foo(x) }
var dummy = zzz;
++x;
dummy = zzz;
++x;
dummy = zzz;
