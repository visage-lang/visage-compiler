/**
 * Regression test JFXC-3236 : Slacker Binding: script-private non-overridden/object-literal-inited
 *
 * @compilearg -XDoptstats="i"
 * @test/warning
 */


public class jfxc3236 {
  var a = 3; // bound to
  def b = bind a;
}
