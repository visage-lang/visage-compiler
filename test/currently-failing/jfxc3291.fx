/**
 * currently-failing test JFXC-3291 : bind fails from override var to a normal var
 *
 * Should print: urx set to: 300
 * And printed value of urx should be 300
 *
 * @compilefirst jfxc3291PT.fx
 * @test
 * @run
 */

class jfxc3291 extends jfxc3291PT {
  public var width : Integer;
  var rx = bind width;
  override var urx = bind rx on replace {
    println("urx set to: {urx}")
  }
}

def obj = jfxc3291 { width: 300 }
println("width value: {obj.width}");
println("rx value: {obj.rx}");
println("urx value (should be 300): {obj.urx}");

