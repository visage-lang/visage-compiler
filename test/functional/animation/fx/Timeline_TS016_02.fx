/*
 * Timeline_TS016_02.fx

 * @test
 * @run
 */

/**
 * @author Baechul Kim
 */

import javafx.animation.*;
import javafx.lang.Duration;

import java.lang.System;
import java.lang.Thread;
import java.lang.AssertionError;

var t: Timeline = Timeline {
    repeatCount: 1
    autoReverse: false
    keyFrames: [
        KeyFrame {
            canSkip: false
            time  : 10ms
        }
   ]
}

function terminate(ms: String) {
    t.stop();
    throw new AssertionError(ms);
}

t.play();

function checkRunningStatus() {
    if(t.running != false) {
       terminate("t.running = {t.running}: should false.");
    }
}

// timeline should be ended after 1s. So check the running status is false.
Timeline {
    keyFrames: KeyFrame {
        time: 1000ms,
        action: checkRunningStatus
    }
}.play();