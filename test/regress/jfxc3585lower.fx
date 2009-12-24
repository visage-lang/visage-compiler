/**
 * JFXC-3585 : Compiled bind: bound sequence: slice
 *
 * On-replace -- changes to lower
 *
 * @test
 * @run
 */

class jfxc3585lower {
  var seq = ['a', 'b', 'c', 'd', 'e', 'f', 'g'];
  var a = 2;
  var b = 7;
  def bsi = bind seq[a..b] on replace [st..en] = newV { println("[{st}..{en}] = {sizeof newV}") };

  function doit() {
    println("=== 4");
    a = 4; 
    println("=== 0");
    a = 0;
    println("=== -4");
    a = -4;
    println("=== 1");
    a = 1;
  }
}

jfxc3585lower{}.doit()
