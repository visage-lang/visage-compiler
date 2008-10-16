/**
 * Regression test JFXC-2217 : On assignment, object literal init, or initial value, convert (non-literal) nullable value
 *
 * @test
 * @run/fail
 */

class A {
  var s : String;
}

function run() {
  var e = if (false) "pop" else null;
  println("init if: {e}");
  var f : String;
  f = if (false) "pop" else null;
  println("assign if: {f}");
}
