/**
 * Regression test for JFXC-1044: KeyFrame.action functions are not invoked in proper time
 *   (for multiple repeat cycles)
 *
 * @test
 * @run
 */

import javafx.animation.*;
import javafx.lang.Duration;
import java.lang.System;
import java.lang.Thread;

class CLS {
    public attribute a:Number = -1;
}

var cls = CLS {};

private function makeKF(n:Number):KeyFrame {
    KeyFrame {
        time: Duration { millis: n }
        values: cls.a => n
        action: function() {
            System.out.println("timeline tick {n}ms - {cls.a}");
        }
    }
}

var t: Timeline = Timeline {
    repeatCount: 3.0
    keyFrames: [
        for (n in [0..3]) makeKF(n)
    ]
};

System.out.println("starting timeline (autoReverse=false, toggle=false)");
t.autoReverse = false;
t.toggle = false;
t.start();
Thread.sleep(200);
System.out.println("timeline finished (isRunning()={t.isRunning()})");

System.out.println("starting timeline (autoReverse=false, toggle=true)");
t.toggle = true;
t.start();
Thread.sleep(200);
System.out.println("timeline finished (isRunning()={t.isRunning()})");

System.out.println("starting timeline (autoReverse=false, toggle=true)");
t.start();
Thread.sleep(200);
System.out.println("timeline finished (isRunning()={t.isRunning()})");

System.out.println("starting timeline (autoReverse=true, toggle=false)");
t.autoReverse = true;
t.toggle = false;
t.start();
Thread.sleep(200);
System.out.println("timeline finished (isRunning()={t.isRunning()})");

System.out.println("starting timeline (autoReverse=true, toggle=true)");
t.toggle = true;
t.start();
Thread.sleep(200);
System.out.println("timeline finished (isRunning()={t.isRunning()})");

System.out.println("starting timeline (autoReverse=true, toggle=true)");
t.start();
Thread.sleep(200);
System.out.println("timeline finished (isRunning()={t.isRunning()})");
