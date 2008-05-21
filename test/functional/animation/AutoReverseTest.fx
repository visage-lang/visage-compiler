/**
  * Checks timeline's autoReverse attribute
  *
  * @test
  * @run
  */

import javafx.animation.*;
import java.lang.*;

var images = [1..16];
var golden = [[0..15], [14..0 step -1]];
var id: Integer = 0;

var t : Timeline = Timeline {
    autoReverse: true
    repeatCount: Double.POSITIVE_INFINITY
    keyFrames: for (image in images)
        KeyFrame {
            time: 100ms * indexof image
            action: function() {
                if(id == sizeof golden) {
                    System.out.println("pass");
                    t.stop(); 
                } else if(indexof image <> golden[id++]) {
                    System.out.println("false");
                    t.stop();
                }
            }
        }
}

t.start();
