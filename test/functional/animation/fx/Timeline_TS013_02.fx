/*
 * Timeline_TS013_02.fx
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

var k: KeyFrame = KeyFrame {};

//System.out.println("Default KeyFrame.action = {k.action}");
//System.out.println("Default KeyFrame.canSkip = {k.canSkip}");
//System.out.println("Default KeyFrame.time = {k.time}");
//System.out.println("Default KeyFrame.timelines = " + k.timelines);
//System.out.println("Default KeyFrame.values = " + k.values);


if(k.action != null) {
	throw new AssertionError("test failed: action should be 'null'");
} 

if(k.canSkip != false) {
	throw new AssertionError("test failed: canSkip should be 'false'");
} 

if(k.time != 0s) {
	throw new AssertionError("test failed: time should be '0s'");
}

if(sizeof k.timelines != 0) {
	throw new AssertionError("test failed: timelines should have be an empty sequence");
}

if(sizeof k.values != 0) {
	throw new AssertionError("test failed: values should have be an empty sequence");
} 