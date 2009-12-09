/**
 * Regression test JFXC-3676 : Compiled bind: applyDefaults and VarInit
 *
 * @test
 * @run
 */

function run() {
  var x = y + 1000;
  var y = 7;
  println(x);
}
