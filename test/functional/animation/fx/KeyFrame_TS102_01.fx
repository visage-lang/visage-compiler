/*
 * KeyFrame_TS102_01.fx
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
import java.util.concurrent.TimeUnit;

function runLater(ms: Number, f: function(): Void): Void {
    Timeline {
        keyFrames: KeyFrame {
            time: Duration.valueOf(ms)
            action: f
        }
    }.play();
} 

function getDuration(begin: Number): Number {
	TimeUnit.NANOSECONDS.toMillis((System.nanoTime()-begin).longValue());
}

var ea: Boolean = false;
var eb: Boolean = false;
var ec: Boolean = false;

var s1: Number;
var t0: Number;
var t1: Number;
var t2: Number;

var keepAlive : Timeline = Timeline {
	repeatCount: Timeline.INDEFINITE
    keyFrames: KeyFrame {
		time: 100ms
	}
};

var t : Timeline = Timeline {
    keyFrames: [
		KeyFrame {
			time: -1s
			canSkip: false
			action: function() {				
				ea = true;
				t0 = getDuration(s1);
				//System.out.println("{t0} (set time:  -1000ms)");
			}
		},		
		KeyFrame {
			time: 1s
			canSkip: false
			action: function() {
				ec = true;
				t2 = getDuration(s1);
				//System.out.println("{t2} (set time:  1000ms)");
			}
		},
		KeyFrame {
			time: 0s
			canSkip: false
			action: function() {				
				eb = true;
				t1 = getDuration(s1);
				//System.out.println("{t1} (set time:  0ms)");
			}
		},
	]
};

keepAlive.play();
s1 = System.nanoTime();
t.play();
runLater(5000, check);

function check() {
	//System.out.println("checking");
	keepAlive.stop();

	// undocumented behavior.
	if(t0 > t1 or t0 > t2) {
		throw new AssertionError("test failed");
	}

	if(ea == false or eb == false or ec == false) {
		throw new AssertionError("test failed");
	}
}
