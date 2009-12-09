/**
 * Regression test JFXC-3586 : Compiled bind: ABORT not implemented JFXIndexof - non-bound-for with bound-index-of
 *
 * Original
 *
 * @test
 * @run
 */

class Path {
  var fill: Integer
}

var rng = [0..3];

for (xx in rng) {
    Path {
        fill: bind indexof xx;
    }
}
