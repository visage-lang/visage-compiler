/*
 * Lang003_TS200_01.fx
 *
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
import java.lang.Throwable;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;


var s1: Number;
var t0: Number;
var t1: Number;
var t2: Number;
var t3: Number;
var t4: Number;

function getDuration(begin: Number): Number {
	TimeUnit.NANOSECONDS.toMillis((System.nanoTime()-begin).longValue());
}

var t: Timeline = Timeline {
    repeatCount: 1
    autoReverse: false
    keyFrames: [
		KeyFrame {
            canSkip: false
            time  : 0ms
            action: function() {
                t0 = getDuration(s1);
				//System.out.println("t0(0) = {t0} [Diff = {t0-0} ms]");
            }
        },
        KeyFrame {
            canSkip: false
            time  : 100ms
            action: function() {
                t1 = getDuration(s1);
				//System.out.println("t1(100) = {t1} [Diff = {t1-100} ms]");
            }
        },
        KeyFrame {
            canSkip: false
            time  : 1s
            action: function() {
                t2 = getDuration(s1);
				//System.out.println("t2(1000) = {t2} [Diff = {t2-1000} ms]");
            }
        },
		KeyFrame {
            canSkip: false
            time  : 0.1m
            action: function() {
                t3 = getDuration(s1);
				//System.out.println("t3(6000) = {t3} [Diff = {t3-6000} ms]");
            }
        },
        KeyFrame {
            canSkip: false
            time  : 0.01h	// 36s
            action: function() {
                t4 = getDuration(s1);
				//System.out.println("t4(36000) = {t4} [Diff = {t4-36000} ms]");
            }
        }
   ]
}

s1 = System.nanoTime();
t.play();

// check after 1m
var timer = new Timer(60000, ActionListener {
    public function actionPerformed(e: ActionEvent) {
		if(not (t0 < t1 and t1 < t2 and t2 < t3 and t3 < t4)) {
			throw new AssertionError("keyFrames weren't evaluated in order");
		}
    }
});
timer.setRepeats(false);
timer.start();
