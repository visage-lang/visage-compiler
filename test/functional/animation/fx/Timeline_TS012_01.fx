/*
 * Timeline_TS012_01.fx
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

var target: Integer = 1;

var t: Timeline = Timeline {
    repeatCount: 0
    keyFrames: [
        KeyFrame {
            time: 100ms
			values: target => 3
			action: function() {
				//System.out.println("evaluated at 100ms");
				throw new AssertionError("test failed");
			}
        },
		KeyFrame {
			time: 1500ms
		}
    ]
};

t.start();

runLater(1000, check);
function check() {
	if(target == 3) {
		throw new AssertionError("test failed");
	}
}
