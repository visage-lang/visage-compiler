/**
 * Regression test JFXC-2025 : Elide unbound script-private member vars
 *
 * negative test -- what shouldn't be elided
 *
 * @compilearg -XDoptstats="is"
 * @test/warning
 */

var x = 1; // bound to
def y = bind x; // bind def

public class Foo {
  var a = 3; // bound to
  def b = bind a;  // bind def
  var c = 0 // isInitialied
     on replace { if (isInitialized(c)) 0 else 1 }
}
