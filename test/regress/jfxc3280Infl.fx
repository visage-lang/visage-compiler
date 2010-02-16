/**
 * JFXC-3280 : override var x on replace... fails in a Stage initializer
 *
 * @compilefirst jfxc3280InflStooge.fx
 * @test
 * @run
 */

function run() {
  def sto0 = jfxc3280InflStooge { }
  sto0.x = 11.11;
  var lbx = bind sto0.x;
  sto0.x = 22.22;
  
  def sto1 = jfxc3280InflStooge {
    override var x on replace { println("override {x}") }
  }
  sto1.x = 11.11;
  var lbx1 = bind sto0.x;
  sto1.x = 22.22;
}
