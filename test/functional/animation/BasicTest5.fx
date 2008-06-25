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
    repeatCount: 100
    keyFrames: [
        KeyFrame {
            time: 1000ms
        }
    ]
}

var keepalive: Timeline = Timeline {
    repeatCount: 100
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
    keepalive.start();
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
    if(t.paused) {
        System.out.println("pass - pause");
    }

    // checks if it can be stopped from the paused state.
    t.stop();
    launchIn(3000, f4);
}

function f4() {
    if(not t.running) {
        System.out.println("pass - stop");
    }
    keepalive.stop();
}

f1();
