/**
 * Should-fail test for JFXC-2507 - NullPointerException in 
 * syntactic analysis when using "at (Duration.valueOf(someNumber))".
 *
 * Compile should fail but we should get proper error messages rather
 * than NPE.
 *
 * @test/compile-error
 */

package javafxapplication;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;

var variable = 1;
var timeToFly = 3.0;
var firstTimeline = Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: [
        KeyFrame {
            time: Duration.valueOf(timeToFly);
            canSkip: true
            values: [
                variable => 10 tween Interpolator.LINEAR
            ]
        }
    ]
}
var secondTimeline = Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: [
        at (Duration.valueOf(timeToFly)) {
            variable => 10 tween Interpolator.LINEAR
        }
    ]
}
