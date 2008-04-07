/**
 * Regression test for JFXC-1044: KeyFrame.action functions are not invoked in proper time
 *   (for one cycle only)
 *
 * @test
 * @run
 */

import javafx.animation.*;
import javafx.lang.Duration;
import java.lang.System;

class CLS {
    public attribute a:Number = -1;
}

var cls = CLS {};

private function makeKF(n:Number):KeyFrame {
    KeyFrame {
        time: Duration { millis: n }
        values: cls.a => n
        action: function() {
            System.out.println("timeline tick {n}ms - {cls.a}");
        }
    }
}

var t: Timeline = Timeline {
    keyFrames: [
        for (n in [0..20]) makeKF(n),
        KeyFrame {
            time: 25ms
            values: cls.a => 25.0
            action: function() {
                System.out.println("timeline finished - {cls.a}");
            }
        }
    ]
};

t.start();
