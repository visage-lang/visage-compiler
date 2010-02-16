/**
 * JFXC-3280: override var x on replace... fails in a Stage initializer
 *
 * @test
 * @run
 */

public class jfxc3280oops {
  public var a = 0 on replace { println(a); a = 4 }
  var c = 0 on replace { a = 4 }
  var d = 0;
}

function run() {
  var x = jfxc3280oops{}
  println("x.a = {x.a}");
  x.a = 2;
  println("x.a = {x.a}");
}
