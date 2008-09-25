/*
 * KeyValue_TS200_01.fx
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
import java.util.concurrent.TimeUnit;
import com.sun.javafx.runtime.PointerFactory;

var pf: PointerFactory = PointerFactory {};
var s:String = "abc";
var bps = bind pf.make(s); 
var ps = bps.unwrap();

var i:Integer = 0;
var bpi = bind pf.make(i); 
var pi = bpi.unwrap();

var b:Boolean = true;
var bpb = bind pf.make(b); 
var pb = bpb.unwrap();

var n: Number = 1.0; 
var bpn = bind pf.make(n); 
var pn = bpn.unwrap();

var d:Duration = 1s;
var bpd = bind pf.make(d); 
var pd = bpd.unwrap();

var t : Timeline = Timeline {
    keyFrames: [		
		KeyFrame {
			time: 1s
			values: [
				KeyValue {
					target: ps 
					value: "def"
				},
				KeyValue {
					target: pi 
					value: 10
				},
				KeyValue {
					target: pb
					value: false
				},
				KeyValue {
					target: pn 
					value: 10.0
				},
				KeyValue {
					target: pd 
					value: 10s
				}
			]
			action: function() {
				//System.out.println("After:");
				//System.out.println("String: {s}");
				//System.out.println("Integer: {i}");
				//System.out.println("Boolean: {b}");
				//System.out.println("Number: {n}");
				//System.out.println("Duration: {d}");
				
				if(s != "def") {
					throw new AssertionError("test failed: String");
				}
				if(i != 10) {
					throw new AssertionError("test failed: Integer");
				}
				if(b != false) {
					throw new AssertionError("test failed: Boolean");
				}
				if(n != 10.0) {
					throw new AssertionError("test failed: Number");
				}
				if(d != 10s) {
					throw new AssertionError("test failed: Duration");
				}
			}
		}
	]
}

//System.out.println("Before:");
//System.out.println("String: {s}");
//System.out.println("Integer: {i}");
//System.out.println("Boolean: {b}");
//System.out.println("Number: {n}");
//System.out.println("Duration: {d}\n");
t.play();

