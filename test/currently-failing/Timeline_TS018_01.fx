/*
 * Timeline_TS018_01.fx
 *
 * @test
 */

/**
 * was: run
 * (this test should be restored after RT-5306 is fixed)
 * @author Baechul Kim
 */

import javafx.animation.*;
import javafx.lang.Duration;
import java.lang.System;
import java.lang.Thread;
import java.lang.AssertionError;
import java.lang.Throwable;

function runLater(ms: Number, f: function(): Void): Void {
    Timeline {
        keyFrames: KeyFrame {
            time: Duration.valueOf(ms)
            action: f
        }
    }.play();
} 

var images = [1..16];
var golden: Integer[] = [[0..15]]; 
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
                if(indexof image == 8) {
                    t.pause();
                }
            }
        }
}

keepAlive.play();
t.play();

runLater(2000, resume);

function resume() {
	//System.out.println("t.paused = {t.paused}. Now will resume.");
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
