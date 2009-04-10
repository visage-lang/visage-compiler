/*
 * Timeline_TS013_01.fx
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

var t: Timeline = Timeline {};

//System.out.println("Default Timeline.autoReverse = {t.autoReverse}");
//System.out.println("Default Timeline.keyFrames = {t.keyFrames}");
//System.out.println("Default Timeline.paused = {t.paused}");
//System.out.println("Default Timeline.repeatCount = {t.repeatCount}");
//System.out.println("Default Timeline.running = {t.running}");
//System.out.println("Default Timeline.currentRate = {t.currentRate}");
//System.out.println("Default Timeline.rate = {t.rate}");

if(t.autoReverse != false) {
	throw new AssertionError("test failed: autoReverse should be 'false'");
} 

if(sizeof t.keyFrames != 0) {
	throw new AssertionError("test failed: keyFrames should have be an empty sequence");
} 

if(t.paused != false) {
	throw new AssertionError("test failed: paused should be 'false'");
} 

if(t.repeatCount != 1.0 ) {
	throw new AssertionError("test failed: repeatCount should be 1");
} 

if(t.running != false) {
	throw new AssertionError("test failed: running should be 'false'");
} 

if(t.rate <= 0) {
	throw new AssertionError("test failed: toggle should be greater than 0");
} 
