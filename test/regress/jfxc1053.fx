/**
 * Regression test for JFXC-1053: Sometimes KeyFrame.action not invoked for subtimelines
 *
 * @test
 * @run
 */

import java.lang.System;
import javafx.animation.*;
import javafx.lang.Duration;

var t = Timeline {
    keyFrames:
    KeyFrame {
        timelines:
        Timeline {
            keyFrames: [
                KeyFrame { time: 0ms   action: function() { System.out.println("started"); } },
                KeyFrame { time: 100ms action: function() { System.out.println("stopped"); } }
            ]
        }
    }
}

t.play();
