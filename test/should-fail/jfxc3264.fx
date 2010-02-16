/*
 * @test/compile-error
 */
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;

var val1 = [1.0, 200.0, 250.0, 300.0];

var timeLine1 = Timeline {
    keyFrames: [
        KeyFrame { 
            time: 2s values: val1[0] => 300 tween Interpolator.LINEAR
        }
    ]
}
