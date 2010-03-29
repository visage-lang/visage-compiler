/**
 * JFXC-3247 : Slacker Binding: general case
 *
 * @compilefirst jfxc3247sub.fx
 * @test
 * @run
 */

class jfxc3247super extends jfxc3247sub {
  override var b on replace { println("jfxc3247super.b {b}") }
  override var c = 234;
  override var d = bind x * 100;
}

function run() {
  def m = jfxc3247super {
    e: 123
    f: bind 666
  }
  m.b;
  m.show();
  m.x = 7777;
  m.show();
}
