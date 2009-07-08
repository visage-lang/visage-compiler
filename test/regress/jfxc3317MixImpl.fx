/**
 * JFXC-3317 : Optimize bound JavaFX var select with mutable selectors
 *
 * @compilefirst jfxc3317Mix.fx
 * @compilefirst jfxc3317Impl.fx
 * @test
 * @run
 */

class jfxc3317MixImpl {
  public var m : jfxc3317Mix;
  public def bt = bind m.text;
}

function run() {
  var im = jfxc3317Impl{ text: "Yo" }
  var mi = jfxc3317MixImpl { m : im }
  var btl = mi.bt;
  println(btl);
}
