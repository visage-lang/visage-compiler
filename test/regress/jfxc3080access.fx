/**
 * Regression test JFXC-3080 : Statements executed out of order
 *
 * @test
 * @run
 */

class Foo {
}
function run() {
  var myNode = "bop";
  var fV = myNode;
  Foo {
    function blip() { fV }
  }
  println(fV);
}
