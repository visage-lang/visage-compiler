/**
 * Regression test JFXC-2278 : Attribution should catch override of instance var when no 'override' keyword
 *
 * @compilefirst jfxc2278OKSub.fx
 * @test
 * @run
 */

class jfxc2278OK extends jfxc2278OKSub {
  public var x;
  public def y = 6;
}

var z = jfxc2278OK{ x: 'yada' }
println(z.x);
