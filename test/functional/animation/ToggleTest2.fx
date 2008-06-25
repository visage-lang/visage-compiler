/**
  * Checks timeline toggle attribute
  *
  * @test
  * @run
  */

import javafx.animation.*;
import java.lang.*;
import javax.swing.Timer;
import java.awt.event.*;

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
    t.toggle = false;
    System.out.println("toggle: {t.toggle}");
    t.start();
    launchIn(3000, f2);
}

function f2() {
    t.toggle = true;
    System.out.println("toggle: {t.toggle}");
    t.start();
    launchIn(3000, f3);
}

function f3() {
    t.start();
    launchIn(3000, f4);
}

function f4() {
    keepalive.stop();
}

f1();
