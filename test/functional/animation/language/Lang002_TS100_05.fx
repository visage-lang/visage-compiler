/*
 * Lang002_TS100_05.fx

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


var s:String = "abc";
var i:Integer = 0;
var b:Boolean = true;
var n:Number = 1.0;
var d:Duration = 1s;

class XYZ {
    public var s:String = "abc";
    public var i:Integer = 0;
    public var b:Boolean = true;
    public var n:Number = 1.0;
    public var d:Duration = 1s;
}

var xyz = XYZ{};

var t = Timeline {
    repeatCount: 1
    autoReverse: false
    keyFrames: [
        KeyFrame {
            time  : 1s
            values: [
				s => "def" tween Interpolator.DISCRETE, 
				i => 10 tween Interpolator.DISCRETE, 
				b => false tween Interpolator.DISCRETE, 
				n => 10 tween Interpolator.DISCRETE, 
				d => 10s tween Interpolator.DISCRETE]
            action: function() {
                if(s != "def") {
                    throw new AssertionError("=>String: failed");
                }
                if(i != 10) {
                    throw new AssertionError("=>Integer: failed");
                }
                if(b != false) {
                    throw new AssertionError("=>Boolean: failed");
                }
                if(n != 10) {
                    throw new AssertionError("=>Number: failed");
                }
                if(d != 10s) {
                    throw new AssertionError("=>Duration: failed");
                }
            }
        },
        KeyFrame{
            time  : 2s
            values: [
				xyz.s => "def" tween Interpolator.DISCRETE, 
				xyz.i => 10 tween Interpolator.DISCRETE, 
				xyz.b => false tween Interpolator.DISCRETE, 
				xyz.n => 10 tween Interpolator.DISCRETE,
				//xyz.d => 10s
				]
            action: function() {
                if(xyz.s != "def") {
                    throw new AssertionError("=>String: failed");
                }
                if(xyz.i != 10) {
                    throw new AssertionError("=>Integer: failed");
                }
                if(xyz.b != false) {
                    throw new AssertionError("=>Boolean: failed");
                }
                if(xyz.n != 10) {
                    throw new AssertionError("=>Number: failed");
                }
                if(xyz.d != 10s) {
                    //throw new AssertionError("=>Duration: failed");
                }
            }
        }
   ]
}

t.start();
