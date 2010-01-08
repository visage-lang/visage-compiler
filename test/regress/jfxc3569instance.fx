/**
 * regression test: JFXC-3569 : Compiled bind: sequence actions (non-bound)
 *
 * @test
 * @run
 */

class jfxc3569instance {
  var seq : String[]
}

function run() {
  def z = jfxc3569instance{}
  z.seq =  ["row", "row", "mow"];
  println(z.seq[1..2] = "boat");
  println(z.seq);
  println(z.seq[0..0] = ["little", "tiny"]);
  println(z.seq);
  println(z.seq = ["no"]);
  println(z.seq);
}
