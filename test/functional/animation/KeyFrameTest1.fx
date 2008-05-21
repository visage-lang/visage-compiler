/**
  * Checks the frame actions are being called properly at a stress situation when
  * canSkip is false. 
  * 
  * @test
  * @run
  */


import javafx.animation.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;

var count: Integer = 0;
var maxLoad: Integer = 500;

var t: Timeline = Timeline {
    repeatCount: 50
    keyFrames: [
        KeyFrame {
            canSkip: false
            time: 16ms            
            action: function() {
                count++;
                System.out.println("{count}");
                for(i in [0..maxLoad]) {
                    Thread.sleep(2);
                }
            }
        }
    ]
}

t.start();
Thread.sleep(3000);
