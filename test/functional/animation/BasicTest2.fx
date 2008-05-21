/**
  * Checks the timing interval.
  *
  * @test
  * @run
  */

import javafx.animation.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;

var beginNano: Number;
var threshold = 200; 

var t: Timeline = Timeline {
    keyFrames: [
        KeyFrame {
            time: 1000ms
            action: function() {
                beginNano = System.nanoTime();
            }
        },
        KeyFrame {
            time: 2000ms
            action: function() {        
                var elapsed: Number = TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - beginNano).longValue());
                if(elapsed > (1000-threshold) and elapsed < (1000+threshold)) {
                    System.out.println("pass");
                } else {
                    System.out.println("fail");
                }
            }
        }
    ]
}

t.start();
