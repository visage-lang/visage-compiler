/**
  * Checks various timeline functions.
  *
  * @test
  * @run
  */


import javafx.animation.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;
import javax.swing.Timer;
import java.awt.event.*;

var t: Timeline = Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: [
        KeyFrame {
            time: 1000ms
        }
    ]
}

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
    t.start();
    launchIn(3000, f2);
}

function f2() {
    System.out.println("start : t.running = {t.running}");
    t.pause();
    launchIn(3000, f3);
}

function f3() {
    System.out.println("pause : t.running = {t.running}");
    // If the t.resume() and t.stop() calls are removed, the timeline is never stopped
    t.resume();
    launchIn(3000, f4);
}

function f4() {
    System.out.println("resume: t.running = {t.running}");
    t.stop();
}

f1();