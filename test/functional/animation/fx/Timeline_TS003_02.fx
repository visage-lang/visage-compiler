/*
 * Timeline_TS003_02.fx

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

var images = [1..10];
var golden = [[0..9], [0..9]];
var id: Integer = 0;

var t : Timeline = Timeline {
    autoReverse: true
    repeatCount: 2
    keyFrames: for (image in images)
        KeyFrame {
            time: 100ms * indexof image
            action: function() {
				//System.out.println("=> {indexof image}");

                if(indexof image != golden[id++]) {
                    t.stop();
					throw new AssertionError("autoReverse test failed");
                }
            }
        }
}

function runLater(ms: Number, f: function(): Void): Void {
	var timer = new Timer(ms, ActionListener {
		public function actionPerformed(e: ActionEvent) {
			f();
		}
	});
	timer.setRepeats(false);
	timer.start();
}

t.start();

runLater(400, pause);
runLater(500, resume);

function pause() {
	t.pause();
	//System.out.println("t.paused = {t.paused}");
	t.autoReverse = false;
	return;	// without this why it return Boolean???
}

function resume() {
	t.resume();
	//System.out.println("t.paused = {t.paused}");
}
