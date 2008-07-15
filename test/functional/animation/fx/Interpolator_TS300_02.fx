/*
 * Interpolator_TS300_02.fx
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
import com.sun.javafx.runtime.PointerFactory;

function runLater(ms: Number, f: function(): Void): Void {
	var timer = new Timer(ms, ActionListener {
		public function actionPerformed(e: ActionEvent) {
			f();
		}
	});
	timer.setRepeats(false);
	timer.start();
}

var keepAlive : Timeline = Timeline {
	repeatCount: Timeline.INDEFINITE
    keyFrames: KeyFrame {
		time: 100ms
	}
};

var avgBegin: Number = 0;
var avgMiddle: Number = 0;
var avgEnd: Number = 0;

var count: Integer = 0;
var begin: Number = 0;
var s: Number = 0;

function log(t: Number, dv: Number) {
	var et: Number = TimeUnit.NANOSECONDS.toMillis((t - begin).longValue()) as Number;
	if(dv != 0) {
		count++;
		if(s == 0) {
			s = t;
		} else {
			var dt = TimeUnit.NANOSECONDS.toMillis((t - s).longValue()) as Number;
			dv = dv * 1000.0;
			var slope = dv/dt;
			s = t;
			
			if(et < 100) {
				if(avgBegin == 0) {
					avgBegin = slope;
				} else {
					avgBegin = (avgBegin+slope)/2;
				}
			} else if (et > 900 and et < 1000) {
				if(avgEnd == 0) {
					avgEnd = slope;
				} else {
					avgEnd = (avgEnd+slope)/2;
				}
			} else if(et > 200 and et < 800) {
				if(avgMiddle == 0) {
					avgMiddle = slope;
				} else {
					avgMiddle = (avgMiddle+slope)/2;
				}
			}
		}
	}
}

var pf: PointerFactory = PointerFactory {};
var n: Number = 0 on replace old = newValue {
	log(System.nanoTime(), newValue-old);

	var k: Integer = newValue*100 as Integer;
	for(i in [0..k]) {
		//System.out.print(".");
	}
	//System.out.println("{%.1f newValue*100}");
}
var bpn = bind pf.make(n); 
var pn = bpn.unwrap();

var keyValue = KeyValue {
	target: pn
	value: 1.0
}

var t = Timeline {
    keyFrames: [
        KeyFrame {
            time: 1s
			values: keyValue
        }
	]
}

keepAlive.start();

//System.out.println("\n<Interpolator.LINEAR>");
keyValue.interpolate = Interpolator.LINEAR;
t.start();
begin = System.nanoTime();
runLater(2000, check);

function check() {
	keepAlive.stop();
	//System.out.println("Average begin slope  : {avgBegin}");
	//System.out.println("Average middle slope : {avgMiddle}");
	//System.out.println("Average end slope    : {avgEnd}");
	//System.out.println("Total time ticks(FPS): {count} per 1s");

	if(count < 30) {
		throw new AssertionError("test failed: count");
	}

	if(avgBegin > 1.5 or avgBegin < 0.3) {
		throw new AssertionError("test failed: avgBegin");
	}

	if(avgMiddle > 1.5 or avgMiddle < 0.5) {
		throw new AssertionError("test failed: avgMiddle");
	}

	if(avgEnd > 1.5 or avgEnd < 0.5) {
		throw new AssertionError("test failed: avgEnd");
	}
}
