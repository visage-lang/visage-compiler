/**
 * Regression test JFXC-3653 : Compiled bind: bound sequence: block
 *
 * On replace
 *
 * @test
 * @run
 */

class jfxc3653onr {
  var x = 5;
  var y = 50;
  def bs = bind {
              def low = bind x * 10;
              def high = bind y * 2;
              [low..<high]
           } on replace [a..b] = newVal { println("[{a}..{b}] = {sizeof newVal}") };
  function test() {
    x = 7;
    x = 0;
    y = 25;
    x = 2;
  }
}

jfxc3653onr{}.test()
