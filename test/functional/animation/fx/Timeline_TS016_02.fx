/*
 * Timeline_TS016_02.fx

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

import javax.swing.Timer;
import java.awt.event.*;

var t: Timeline = Timeline {
    repeatCount: 1
    autoReverse: false
    keyFrames: [
        KeyFrame {
            canSkip: false
            time  : 10ms
        }
   ]
}

function terminate(ms: String) {
	t.stop();
    throw new AssertionError(ms);
}

t.start();

// timeline should be ended after 1s. So check the running status is false.
var timer = new Timer(1000, ActionListener {
    public function actionPerformed(e: ActionEvent) {
		if(t.running != false) {
			terminate("t.running = {t.running}: should false.");
		}
    }
});
timer.setRepeats(false);
timer.start();

