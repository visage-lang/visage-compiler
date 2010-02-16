/**
 * JFXC-3317 : Optimize bound JavaFX var select with mutable selectors
 *
 * @compilefirst jfxc3317InitSubA.fx
 * @compilefirst jfxc3317InitSubB.fx
 * @test
 * @run
 */

var jj2 = bind rot.x;
var rot : jfxc3317InitSubB;

function run() {
  def anm = jfxc3317Init{}
  anm.rot = jfxc3317InitSubB{}
  println(anm.jj2);
}
