/**
 * Should fail test JFXC-2591 : Crash -- zero checking of target of KeyValue
 *
 * @test/compile-error
 */

import javafx.animation.*;

var val = 100.0 on replace { println(val) }

var tl = Timeline {
  keyFrames: [
    at (2s) {
      28 => 100.0 tween Interpolator.EASEBOTH
    },
    at (3s) {
      val+val => 100.0 tween Interpolator.EASEBOTH
    }
  ]
};
tl.play();
