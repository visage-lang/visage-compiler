/*
 * Timeline_TS010_01.fx
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

var target: Integer;

var t1 : Timeline = Timeline {
    keyFrames: [ 
		KeyFrame {
            time: 1000ms
			values: target => 5
        }
	]
};

target = 3;
//System.out.println("target = {target}");
t1.start();


runLater(500, check);
function check() {
	//System.out.println("target = {target}");
	if( target != 3 ) {
		throw new AssertionError("test failed");
	}
}
