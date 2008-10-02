/**
 * Regression test JFXC-2026 : Elide unassigned and externally unassignable member vars
 *
 * @compilearg -XDoptstats="is"
 * Should be:  (at)test/warning
 * @test
 */

public-read var x = 1;
public def y = 2;

public class Foo {
  public-read var a = 3;
  public def b = 4;
}
