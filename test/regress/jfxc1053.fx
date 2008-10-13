/**
 * Regression test for JFXC-1053: Sometimes KeyFrame.action not invoked for subtimelines
 *
 * @test/nocompare
 * @run
 */

import java.lang.System;
import javafx.animation.*;
import javafx.lang.Duration;
import java.lang.AssertionError;

function runLater(ms: Duration, f: function(): Void): Void {
    Timeline {
        keyFrames: KeyFrame {
            time: ms
            action: f
        }
    }.play();
}

var keepAlive : Timeline = Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: KeyFrame {
        time: 100ms
    }
};

var golden:String = "started/stopped";
var out:String = "";

var t = Timeline {
    keyFrames:
    KeyFrame {
        timelines:
        Timeline {
            keyFrames: [
                KeyFrame { time: 0ms   action: function() { out = "started"; } },
                KeyFrame { time: 100ms action: function() { out = "{out}/stopped"; } }
            ]
        }
    }
}

keepAlive.play();
t.play();

runLater(1s, check);
function check() {
    keepAlive.stop();
    if(t.running) {
        t.stop();
        throw new AssertionError("test failed: t is still running");
    }

    if(out != golden) {
        throw new AssertionError("test failed: {out} != {golden}");
    }
    System.out.println("pass");
}

