/*
 * Timeline_TS010_04.fx
 * 
 * @test
 * @run
 */

/**
 * @author Baechul Kim
 */

import javafx.animation.*;
import javafx.lang.Duration;
import java.lang.System;
import java.lang.Thread;
import java.lang.AssertionError;
import java.lang.Throwable;
import javax.swing.Timer;
import java.awt.event.*;

function runLater(ms: Number, f: function(): Void): Void {
	var timer = new Timer(ms, ActionListener {
		public function actionPerformed(e: ActionEvent) {
			f();
		}
	});
	timer.setRepeats(false);
	timer.start();
}

class XYZ {
    public attribute s:String = "abc";
    public attribute i:Integer = 0;
    public attribute b:Boolean = true;
    public attribute n:Number = 1.0;
    public attribute d:Duration = 1s;
}

var target = XYZ{};

var t1 : Timeline = Timeline {
    keyFrames: [ 
		KeyFrame {
            time: 1000ms
			values: target.s => "def"
        }
	]
};

//System.out.println("target.s = {target.s}");
t1.start();

runLater(500, check);
function check() {
	//System.out.println("target.s = {target.s}");
	if( target.s != "abc" ) {
		throw new AssertionError("test failed");
	}
}


