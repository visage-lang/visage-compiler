/**
 * Regression test JFXC-3653 : Compiled bind: bound sequence: block
 *
 * @test
 * @run
 */

class jfxc3653 {
  var k = 5;
  def bs = bind {
              def low = k * 2;
              def high = k * 3;
              [low..high]
           };
  function test() {
    println(bs);
    k = 7;
    println(bs);
  }
}

jfxc3653{}.test()
