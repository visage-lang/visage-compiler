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
import javax.swing.Timer;
import java.awt.event.*;

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

function launchIn(ms: Integer, func: function(): Void): Void {
    var timer = new Timer(ms, ActionListener {
        public function actionPerformed(e: ActionEvent) {
            func();
        }
    });
    timer.setRepeats(false);
    timer.start();
}

function f1() {
    System.out.println("starting timeline (autoReverse=false, toggle=false)");
    t.autoReverse = false;
    t.toggle = false;
    t.start();
    launchIn(200, f2);
}

function f2() {
    System.out.println("timeline finished (running={t.running})");
    System.out.println("starting timeline (autoReverse=false, toggle=true)");
    t.toggle = true;
    t.start();
    launchIn(200, f3);
}

function f3() {
    System.out.println("timeline finished (running={t.running})");
    System.out.println("starting timeline (autoReverse=false, toggle=true)");
    t.start();
    launchIn(200, f4);
}

function f4() {
    System.out.println("timeline finished (running={t.running})");
    System.out.println("starting timeline (autoReverse=true, toggle=false)");
    t.autoReverse = true;
    t.toggle = false;
    t.start();
    launchIn(200, f5);
}

function f5() {
    System.out.println("timeline finished (running={t.running})");
    System.out.println("starting timeline (autoReverse=true, toggle=true)");
    t.toggle = true;
    t.start();
    launchIn(200, f6);
}

function f6() {
    System.out.println("timeline finished (running={t.running})");
    System.out.println("starting timeline (autoReverse=true, toggle=true)");
    t.start();
    launchIn(200, f7);
}

function f7() {
    System.out.println("timeline finished (running={t.running})");
}

f1();