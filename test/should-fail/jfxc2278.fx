/**
 * Should-fail test JFXC-2278 : Attribution should catch override of instance var when no 'override' keyword
 *
 * @compilefirst jfxc2278Sub.fx
 * @test/compile-error
 */

class jfxc2278 extends jfxc2278Sub {
  var x;
  def y = 6;
}
