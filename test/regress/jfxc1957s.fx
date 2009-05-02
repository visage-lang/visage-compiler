/**
 * Regression test JFXC-1957 : Optimization statistics
 *
 * @compilearg -XDoptstats="s"
 * @test/warning
 */

public var x = 1;
public def y = bind x;
public var x0 = 2;
public def y0 = 3;
var x1 = 2;
def y1 = 3;

public class Foo {
  public var a = 3;
  public def b = bind a;
  public var a0 = 3;
  public def b0 = 4;
  var a1 = 3;
  def b1 = 4;

  function bar() {
     var k = 5;
     def l = 6;
     var m = bind k;
     def n = bind l;
  }
}
