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
    repeatCount: Timeline.INDEFINITE
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

// If the following block(3 lines) is commented, the timeline is never stopped.
t.resume();
Thread.sleep(3000);
System.out.println("resume: t.running = {t.running}");

t.stop();
