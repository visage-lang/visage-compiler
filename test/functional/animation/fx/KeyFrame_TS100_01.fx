/*
 * KeyFrame_TS100_01.fx
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

function getDuration(begin: Number): Number {
	TimeUnit.NANOSECONDS.toMillis((System.nanoTime()-begin).longValue());
}

var golden: Integer[] = [0, 1, 2, 3];
var out: Integer[];

var s1: Number;
var t0: Number;
var t1: Number;
var t2: Number;
var t3: Number;
var t4: Number;
var threshold: Number = 100;

var myFrames = [
	KeyFrame {
		time: 100ms
		action: function() {
			//System.out.println("id = 0");
			insert 0 into out;
			t0 = getDuration(s1)-100;
		}
	},
	KeyFrame {
		time: 1s	// 1000ms
		action: function() {
			//System.out.println("id = 1");
			insert 1 into out;
			t1 = getDuration(s1)-1000;
		}
	},
	KeyFrame {
		time: 0.1m		// 60s/10 = 6s = 6000ms
		action: function() {
			//System.out.println("id = 2");
			insert 2 into out;
			t2= getDuration(s1)-6000;
		}
	},
	KeyFrame {
		time: 0.01h		// 3600s/100 = 36s = 36000ms
		action: function() {
			//System.out.println("id = 3");
			insert 3 into out;
			t3 = getDuration(s1)-36000;
		}
	},
	KeyFrame {
		time: 40000ms	// just to keep alive when 'check' is called at 39000ms.
	}
];

var t : Timeline = Timeline {
    keyFrames: myFrames
};

s1 = System.nanoTime();
t.start();
runLater(39000, check);

function check() {
	// check the order.
	//System.out.println("golden = " + golden);
	//System.out.println("out = " + out);
	if(out != golden) {
		throw new AssertionError("test failed");
	}

	// check the diff times.
	// was 26/21/13/13 on vista/core2duo
	//System.out.println("diff(actual time - set time) for t0(100ms) = {t0}");
	//System.out.println("diff(actual time - set time) for t1(1000ms) = {t1}");
	//System.out.println("diff(actual time - set time) for t2(6000ms) = {t2}");
	//System.out.println("diff(actual time - set time) for t3(36000ms) = {t3}");

	if(t0 < 0 or t0 > threshold) {
		throw new AssertionError("test failed: t0");
	}

	if(t1 < 0 or t1 > threshold) {
		throw new AssertionError("test failed: t1");
	}

	if(t2 < 0 or t2 > threshold) {
		throw new AssertionError("test failed: t2");
	}

	if(t3 < 0 or t3 > threshold) {
		throw new AssertionError("test failed: t3");
	}
}
