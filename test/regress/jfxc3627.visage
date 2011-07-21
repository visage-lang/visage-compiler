/**
 * Regression test JFXC-3627 : Compiled bind: bound sequence: function invocation
 *
 * @test
 * @run
 */

class jfxc3627 {
  function fluff(str : String) : String[] {
    [ "front", str, "back" ]
  }
  var blip = "middle";
  def bx = bind fluff(blip);
  function test() {
    println(bx);
    blip = "slide";
    println(bx);
  }
}

jfxc3627{}.test();
