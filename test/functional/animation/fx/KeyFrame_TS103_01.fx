/*
 * KeyFrame_TS103_01.fx
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
import java.util.concurrent.TimeUnit;

function runLater(ms: Number, f: function(): Void): Void {
    Timeline {
        keyFrames: KeyFrame {
            time: Duration.valueOf(ms)
            action: f
        }
    }.play();
} 

var keepAlive : Timeline = Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: KeyFrame {
        time: 100ms
    }
};

var ea: Integer = 0;
var eb: Integer = 0;
var ec: Integer = 0;

var target: Integer on replace {
    //System.out.println("target => {target}");
}

var t : Timeline = Timeline {
    repeatCount: 20
    keyFrames: [
    KeyFrame {
        time: 100ms
        canSkip: false
        action: function() {				
            ea++;                
            Thread.sleep(500);
        }
    },		
        KeyFrame {
        time: 200ms
        canSkip: true
        values: target => eb
        action: function() {
            eb++;
        }
    },
        KeyFrame {
        time: 300ms
        canSkip: false
        action: function() {				
            ec++;
        }
    },
	]
};

keepAlive.play();
t.play();
runLater(3000, check);

function check(): Void {
    if(t.running) {
        runLater(3000, check);
    } else {
        //System.out.println("checking");
        t.stop();
        keepAlive.stop();

        //System.out.println("1st keyFrame: {ea}");
        //System.out.println("2nd keyFrame: {eb}");
        //System.out.println("3rd keyFrame: {ec}");

        if(eb == 20) {
            throw new AssertionError("test failed: didn't skipped");
        }

        if(ea < 20 or ec < 20) {
            throw new AssertionError("test failed: shouldn't be skipped");
        }
    }
}
