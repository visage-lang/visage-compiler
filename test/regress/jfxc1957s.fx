/**
 * Regression test JFXC-1957 : Optimization statistics
 *
 * @compilearg -XDoptstats="s"
 * @test/warning
 */

public var x = 1;
public def y = bind x;

public class Foo {
  public var a = 3;
  public def b = bind a;

  function bar() {
     var k = 5;
     def l = 6;
     var m = bind k;
     def n = bind l;
  }
}
