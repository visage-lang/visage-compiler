/**
 * JFXC-3280 : override var x on replace... fails in a Stage initializer
 *
 * @compilefirst jfxc3280Stooge.fx
 * @test
 * @run
 */

function run() {
  def sto = jfxc3280Stooge {
    override var x on replace { println("override {x}") }
  }
}
