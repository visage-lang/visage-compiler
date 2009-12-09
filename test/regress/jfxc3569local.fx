/**
 * regression test: JFXC-3569 : Compiled bind: sequence actions (non-bound)
 *
 * @test
 * @run
 */

function run() {
  var seq = ["row", "row", "mow"];
  println(seq[1..2] = "boat");
  println(seq);
  println(seq[0..0] = ["little", "tiny"]);
  println(seq = ["no"]);
  println(seq);
}
