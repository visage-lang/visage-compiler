/**
 * JFXC-3315 : Optimize away bound select processing when selector is immutable
 *
 * Test for bound var selector
 *
 * @test
 * @run
 */

public class jfxc3315bd {
  var x = 7;
  public var vs : jfxc3315bd = null;
  var bvs = bind vs;
  init {
    def lb = bind bvs.x;
    vs = this;
    if (lb != 7) {
      println("BROKEN {lb}");
    }
  }
}

function run() {
  jfxc3315bd{}
}
