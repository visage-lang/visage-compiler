/*
 * Timeline_TS019_01.fx

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

var images = [1..16];
var golden: Integer[] = [[0..15], [0..8]]; 
var out: Integer[];

var id: Integer = 0;

var keepAlive: Timeline = Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: [
        KeyFrame {
            time: 100ms
        }
    ]
};

var t : Timeline = Timeline {
	toggle: true
    repeatCount: 1
    keyFrames: for (image in images)
        KeyFrame {
            time: 100ms * indexof image
            action: function() {
				//System.out.println("=> {indexof image}");
				insert indexof image into out;
                if(indexof image == 8) {
                    t.pause();
                }
            }
        }
}

keepAlive.start();
t.start();

runLater(2000, resume);

function resume() {
	//System.out.println("t.paused = {t.paused}. will change toggle to false");
	t.toggle = false;
	t.resume();
	runLater(2000, restart);
}

function restart() {
	t.start();
	runLater(2000, check);
}

function check() {
	keepAlive.stop();
	t.stop();
	//System.out.println("golden = " + golden);
	//System.out.println("out = " + out);
	if(out != golden) {
		throw new AssertionError(" test failed");
	}
}
