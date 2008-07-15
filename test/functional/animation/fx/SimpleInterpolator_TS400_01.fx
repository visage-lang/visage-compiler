/*
 * SimpleInterpolator_TS400_01.fx
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
import com.sun.scenario.animation.Interpolators;
import java.lang.Float;


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
}

public class MyInterpolator extends SimpleInterpolator {
	public attribute i:com.sun.scenario.animation.Interpolator 
		= Interpolators.getEasingInstance(new Float(0.5), new Float(0.0));
	
	public function curve(t: Number): Number {
		i.interpolate(t.floatValue())
	}
}

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
			
			if(et < 200) {
				if(avgBegin == 0) {
					avgBegin = slope;
				} else {
					avgBegin = (avgBegin+slope)/2;
				}
			} else if (et > 500 and et < 1000) {
				if(avgEnd == 0) {
					avgEnd = slope;
				} else {
					avgEnd = (avgEnd+slope)/2;
				}
			} else if(et > 200 and et < 500) {
				if(avgMiddle == 0) {
					avgMiddle = slope;
				} else {
					avgMiddle = (avgMiddle+slope)/2;
				}
			}
		}
	}
}

var n: Number = 0 on replace old = newValue {
	log(System.nanoTime(), newValue-old);

	var k: Integer = newValue*100 as Integer;
	for(i in [0..k]) {
		//System.out.print(".");
	}
	//System.out.println("{%.1f newValue*100}");
}

var t = Timeline {
    keyFrames:
        KeyFrame {
            time: 1s
            values: n => 1 tween MyInterpolator{}
        }
}

keepAlive.start();
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

	if(avgBegin > avgMiddle or avgMiddle > avgEnd) {
		throw new AssertionError("test failed");
	}
}

