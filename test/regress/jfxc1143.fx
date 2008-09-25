/**
  * Checks timeline toggle var
  *
  * @test
  * @run/fail
  */

import javafx.animation.*;
import java.lang.*;

var ids: Integer[] = [0..15];

var t : Timeline = Timeline {
    repeatCount: 1
    keyFrames: for (id in ids)
        KeyFrame {
            time: 100ms * indexof id
            action: function() {
                System.out.println("id: {id}");
                if(id == 5) {
                    //t.toggle = true;  // don't change toggle but invoke start() even when running.
                    t.rate *= -1;
                    System.out.println("toggled");
                }
            }
        }
};
t.play();
//Thread.sleep(3000);
