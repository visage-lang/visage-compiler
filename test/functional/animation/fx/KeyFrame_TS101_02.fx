/*
 * KeyFrame_TS101_02.fx
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

function runLater(ms: Number, f: function(): Void): Void {
	var timer = new Timer(ms, ActionListener {
		public function actionPerformed(e: ActionEvent) {
			f();
		}
	});
	timer.setRepeats(false);
	timer.start();
}

var ea: Boolean = false;
var eb: Boolean = false;

var keepAlive : Timeline = Timeline {
	repeatCount: Timeline.INDEFINITE
    keyFrames: KeyFrame {
		time: 100ms
	}
};

var t : Timeline = Timeline {
    keyFrames: [
		KeyFrame {
			time: 0s
			canSkip: false
			action: function() {
				//System.out.println("evaluated at 0s");
				ea = true;
			}
		},
		KeyFrame {
			time: 0.00001ms
			canSkip: false
			action: function() {
				//System.out.println("evaluated at 0.00001ms");
				eb = true;
			}
		}
	]
};

keepAlive.start();
t.start();
runLater(100, check);

function check() {
	//System.out.println("checking");
	keepAlive.stop();
	if(ea == false or eb == false) {
		throw new AssertionError("test failed");
	}
}