/**
  * Checks timeline toggle attribute
  *
  * @test
  * @run
  */

import javafx.animation.*;
import java.lang.*;

//var image:Image;

var ids: Integer[] = [0..2];

var t : Timeline = Timeline {
    repeatCount: 1
    keyFrames: for (id in ids)
        KeyFrame {
            time: 100ms * indexof id
            action: function() {
                System.out.println("id: {id}");                               
            }
        }
};

t.toggle = false;
System.out.println("toggle: {t.toggle}");
t.start();

Thread.sleep(3000);
t.toggle = true;
System.out.println("toggle: {t.toggle}");
t.start();

Thread.sleep(3000);
t.start();

Thread.sleep(3000);
