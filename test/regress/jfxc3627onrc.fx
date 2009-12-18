/**
 * Regression test JFXC-3627 : Compiled bind: bound sequence: function invocation
 *
 * @test
 * @run
 */

class jfxc3627onrc {
  function fluff(str : String) : String[] {
    [ "front", str, "back" ]
  }
  var blip = "middle";
  def bx = bind fluff(blip) on replace [a..b] = newVal { println("{a}..{b} = {sizeof newVal}") };
  function test() {
    blip = "slide";
  }
}

jfxc3627onrc{}.test();
