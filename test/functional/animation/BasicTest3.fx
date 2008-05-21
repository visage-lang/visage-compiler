/**
  * Checks various timeline functions.
  *
  * @test
  * @run
  */

import javafx.animation.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;

var t: Timeline = Timeline {
    repeatCount: 1
    keyFrames: [
        KeyFrame {
            time: 500ms
            action: function() {
                System.out.println("1");
            }
        },
        KeyFrame {
            time: 1000ms
            action: function() { 
                System.out.println("3");
                t.stop();
            }
        },
        KeyFrame {
            time: 725ms
            action: function() {
                System.out.println("2");
            }
        }
    ]
}

t.start();
Thread.sleep(3000);
