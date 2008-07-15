/*
 * Timeline_TS012_02.fx
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
		public function actionPerformed(e: ActionEvent) {
			f();
		}
	});
	timer.setRepeats(false);
	timer.start();
}

var count: Integer = 0;

var keepAlive: Timeline = Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: [
        KeyFrame {
            time: 100ms
        }
    ]
};

var t: Timeline = Timeline {
    repeatCount: 10
    keyFrames: [
        KeyFrame {
            time: 100ms
			action: function() {
				count++;
				//System.out.println("count = {count}");
			}
        }
    ]
};

keepAlive.start();
t.start();

runLater(100, modifyRepeatCount);
runLater(2000, check);

function modifyRepeatCount() {
	t.repeatCount = 5;
	return;
}

function check() {
	//System.out.println("checking");
	if(count != 5) {
		throw new AssertionError("test failed");
	}
	keepAlive.stop();
}
