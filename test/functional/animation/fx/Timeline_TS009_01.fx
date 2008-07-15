/*
 * Timeline_TS009_01.fx
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

var golden: Integer[] = [0, 1, 2];
var out: Integer[];

var t1 : Timeline = Timeline {
    keyFrames: [
		KeyFrame {
            time: 0ms
            action: function() {
				//System.out.println("time = 0ms");
				insert 0 into out;
            }
        }, // when times are same, should they be evaluated in order of appearance?
		KeyFrame {
            time: 500ms
            action: function() {
				//System.out.println("time = 500ms - 1");
				insert 1 into out;
            }
        },
		KeyFrame {
            time: 500ms
            action: function() {
				//System.out.println("time = 500ms - 2");
				insert 2 into out;
            }
        },
		// just to ensure the program is alive when "check" is called.
		KeyFrame {
            time: 1000ms
        }
	]
};

t1.start();
runLater(500, check);

function check() {
	//System.out.println("golden = " + golden);
	//System.out.println("out = " + out);
	if( out != golden ) {
		throw new AssertionError("test failed");
	}
}
