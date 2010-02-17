/*
 * JFXC-4147: non-bound sequence object literal initializer for var bound in class is broken
 *
 * @test
 * @run
 */

public class A {
  public var n = "Wilbur";
  public var seq = bind [n];
}

class B {
  var a = A {
    seq: ["Orr"]
  }
  init {
    insert "Harbin" into a.seq;
  }
}

function run() {
  def aa = B{}.a;
  for (i in [0..<sizeof aa.seq]) {
    println("{i}: {aa.seq[i]}");
  }
  for (el in aa.seq) {
    println("{indexof el}: {el}");
  }
  println(sizeof aa.seq)
}
