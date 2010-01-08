/**
 * JFXC-3843 : compiled-bind: compiler crashes while compiling FxTester.
 *
 * @subtest
 */
import javafx.animation.*;
public mixin class FireT {
        public def fps : Timeline = Timeline {
        repeatCount: Timeline.INDEFINITE
        keyFrames: [
            KeyFrame {
                time: 2s
                canSkip: false
                action: function() {
                }
            }
        ]
    }
}
