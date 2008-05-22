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
    repeatCount: 100
    keyFrames: [
        KeyFrame {
            time: 1000ms
        }
    ]
}

t.start();
Thread.sleep(3000);
System.out.println("start : t.running = {t.running}");

t.pause();
Thread.sleep(3000);
System.out.println("pause : t.running = {t.running}");
if(t.paused) {
    System.out.println("pass - pause");
}

// checks if it can be stopped from the paused state.
t.stop();
Thread.sleep(3000);
if(not t.running) {
    System.out.println("pass - stop");
}
