/**
 * Should-fail test JFXC-1630 : Enforce access controls on override var
 *
 * @compilefirst jfxc1630sub.fx
 * @test/compile-error
 */

class jfxc1630 extends jfxc1630sub {
  override var x = 88;
}
