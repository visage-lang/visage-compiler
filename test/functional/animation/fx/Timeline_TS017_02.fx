/*
 * Timeline_TS017_02.fx

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

var images = [1..16];
var golden: Integer[] = [[0..15], [15..0 step -1]]; 
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
    rate: 1
    repeatCount: 1
    keyFrames: for (image in images)
        KeyFrame {
        time: 100ms * indexof image
        action: function() {
            //System.out.println("=> {indexof image}");
            insert indexof image into out;
        }
    }
}

keepAlive.play();
t.play();

runLater(2000, rerun);

function rerun() {
    //System.out.println("t.running = {t.running}. Now will play again.");
    t.rate=-1;
    t.play();
    runLater(2000, check);
}

function check() {
	keepAlive.stop();
	//System.out.println("golden = " + golden);
	//System.out.println("out = " + out);
	if(out != golden) {
		throw new AssertionError(" test failed");
	}
}
