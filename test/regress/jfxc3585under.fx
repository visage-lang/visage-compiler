/**
 * JFXC-3585 : Compiled bind: bound sequence: slice
 *
 * On-replace -- changes to underlying sequence
 *
 * @test
 * @run
 */

class jfxc3585under {
  var seq = ['a', 'b', 'c', 'd', 'e', 'f', 'g'];
  var a = 1;
  var b = 5;
  def bsi = bind seq[a..b] on replace [st..en] = newV { println("[{st}..{en}] = {sizeof newV}") };

  function doit() {
    println("=== insert front");
    insert "@" before seq[0]; 
    println("=== delete front");
    delete seq[0];
    println("=== insert seq[3]");
    insert "X" before seq[3];
    println("=== delete seq[3]");
    delete seq[3];
    println("=== insert");
    insert "Z" into seq;
    println("=== delete seq[7]");
    delete seq[7];
    println("=== assign seq[3]");
    seq[3] = "z";
    println("=== assign seq[0]");
    seq[0] = "A";
    println("=== delete seq[2..4]");
    delete seq[2..4];
    println("=== insert ['c', 'd', 'e'] before seq[2]");
    insert ['c', 'd', 'e'] before seq[2];
    println(bsi);
  }
}

jfxc3585under{}.doit()
