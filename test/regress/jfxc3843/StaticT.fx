/**
 * JFXC-3843 : compiled-bind: compiler crashes while compiling FxTester.
 *
 * @subtest
 */

import javafx.animation.*;
public class StaticT {
   package var colorChanger: Timeline;
   var animColor;
   postinit {
        colorChanger = Timeline {
            repeatCount: Timeline.INDEFINITE
            autoReverse: true
            keyFrames: [
                KeyFrame {
                    time: 0s
                    values: animColor => "Color.RED" tween Interpolator.LINEAR
                },
                KeyFrame {
                    time: 1s
                    values: animColor => "Color.BLUE" tween Interpolator.LINEAR
                }
            ]
        };
    }
}
