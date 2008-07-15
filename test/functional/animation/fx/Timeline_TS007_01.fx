/*
 * Timeline_TS007_01.fx
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

var count: Integer = 0;
function runLater(ms: Number, f: function(): Void): Void {
	var timer = new Timer(ms, ActionListener {
		public function actionPerformed(e: ActionEvent) {
			f();
		}
	});
	timer.setRepeats(false);
	timer.start();
}

var myFrames = [
	KeyFrame {
		time: 0ms
		action: function() {
			//System.out.println("count = {count}");
			count++;
		}
	},
	KeyFrame {
		time: 500ms
		action: function() {
			//System.out.println("count = {count}");
			count++;
		}
	},
	KeyFrame {
		time: 1000ms
		action: function() {
			//System.out.println("count = {count}");
			count++;
		}
	},
	KeyFrame {
		time: 1500ms
		action: function() {
			//System.out.println("count = {count}");
			count++;
		}
	},
	KeyFrame {
		time: 2000ms
		action: function() {
			//System.out.println("count = {count}");
			count++;
		}
	},
	KeyFrame {
		time: 3000ms
		action: function() {
			//System.out.println("count = {count}");
			count++;
		}
	},
	KeyFrame {
		time: 3500ms
		action: function() {
			//System.out.println("count = {count}");
			count++;
			wasRemoved = false;
		}
	}
];

var t1 : Timeline = Timeline {
    keyFrames: myFrames
};

var wasAdded: Boolean = false;
var wasRemoved: Boolean = true;

t1.start();
runLater(1000, addFrame);
runLater(1500, removeFrame);
runLater(5000, check);

// add myFrames[7]
function addFrame() {
	insert KeyFrame {
		time: 2500ms
		action: function() {
			//System.out.println("count = {count}");
			count++;
			wasAdded = true;
		}
	} into myFrames;
	//System.out.println("a keyFrame added");
}

function removeFrame() {
	delete myFrames[6];
	//System.out.println("a keyFrame removed");
}

function check() {
	if(not wasAdded or not wasRemoved) {
		throw new AssertionError("add/remove keyFrame test failed");
	}
}
