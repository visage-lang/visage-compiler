/**
  *
  * @test
  * @run
  */


import javafx.animation.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;
import javafx.lang.Duration; 

var beginNano: Number;
var threshold = 200;

var t1: Duration = 0s;
var t2: Duration = 0.5s;

var t: Timeline = Timeline {
    repeatCount: 1
    keyFrames: [
        KeyFrame {
            time: t1            
            action: function() {
                var elapsed: Number = TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - beginNano).longValue());
                if(elapsed < (threshold)) {
                    System.out.println("test1 - pass");
                } else {
                    System.out.println("test1 - fail");
                }
                beginNano = System.nanoTime();
            }
        },
        KeyFrame {
            time: t2            
            action: function() {
                var elapsed: Number = TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - beginNano).longValue());
                if(elapsed > (500-threshold) and elapsed < (500+threshold)) {
                    System.out.println("test2 - pass");
                } else {
                    System.out.println("test2 - fail");
                }
                beginNano = System.nanoTime();
            }
        },
        KeyFrame {
            time: Duration { millis: 1000 }            
            action: function() {
                var elapsed: Number = TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - beginNano).longValue());
                if(elapsed > (500-threshold) and elapsed < (500+threshold)) {
                    System.out.println("test3 - pass");
                } else {
                    System.out.println("test3 - fail");
                }
                beginNano = System.nanoTime();
            }
        }
    ]
}

beginNano = System.nanoTime();
t.start();

