/*
 * Timeline_TS005_01.fx
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

var count: Integer = 0;

var t: Timeline = Timeline {
    repeatCount: 2
    keyFrames: [
        KeyFrame {
            time: 100ms
			action: function() {
				count++;
				//System.out.println(".count = {count}");
				if(count == 1) {
					t.repeatCount = Timeline.INDEFINITE;
				}
			}
        }
    ]
};

function runLater(ms: Number, f: function(): Void): Void {
	var timer = new Timer(ms, ActionListener {
		public override function actionPerformed(e: ActionEvent) {
			FX.deferAction(f);
		}
	});
	timer.setRepeats(false);
	timer.start();
}

function check() {
	if(not t.running) {
		throw new AssertionError("stop test failed");
	} else {
		t.stop();
	}
}

t.play();
runLater(3000, check);
