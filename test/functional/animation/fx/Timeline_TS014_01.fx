/*
 * Timeline_TS014_01.fx
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

function runLater(ms: Number, f: function(): Void): Void {
	var timer = new Timer(ms, ActionListener {
		public override function actionPerformed(e: ActionEvent) {
			f();
		}
	});
	timer.setRepeats(false);
	timer.start();
}

var count: Integer = 0;
var target: Integer = 2;

var keepAlive: Timeline = Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: [
        KeyFrame {
            time: 100ms
        }
    ]
};

// undocumented when a fraction repeatCount is given.
// repeat count is a Number type. 
var t: Timeline = Timeline {
    repeatCount: 1.5
    keyFrames: [
        KeyFrame {
            time: 100ms
			values: target => 8 tween Interpolator.LINEAR
			action: function() {
				count++;
				//System.out.println("count = {count}");
			}
        }
    ]
};

//System.out.println("t.repeatCount = {t.repeatCount}");
keepAlive.play();
t.play();

runLater(1000, check);
function check() {
	//System.out.println("checking");
	if(target != 5) {	// 5 should be the result of 1.5 repeatCount between 2 and 8.
		throw new AssertionError("test failed");
	}
	keepAlive.stop();
}
