/**
 * Regression test JFXC-3599 : Compiled bind: bound block
 *
 * @test
 * @run
 */

var v = 10;
def bs = bind {
		def sq = v * v;
		def mark = 10000;
		mark + sq
};

class jfxc3599 {
  var d = "oo";
  def b = bind {
		def spl = "{d}xx{d}";
		def dd = "{spl}---{spl}";
		"{dd}===={dd}"
  }

  function doit() {
    println(b);
    d = "@@";
    println(b);
  }
}

println(bs);
v = 5;
println(bs);

jfxc3599{}.doit();
