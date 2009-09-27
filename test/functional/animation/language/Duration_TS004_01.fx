/*
 * Duration_TS004_01.fx

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
import javax.swing.Timer;
import java.awt.event.*;


var t1: Duration = 1.2m;

if(t1.toHours() != 0) {
	throw new AssertionError("duration toHours test failed");
}

if(t1.toMillis() != 72000) {
	throw new AssertionError("duration toMillis test failed");
}

if(t1.toMinutes() != 1) {
	throw new AssertionError("duration toMinutes test failed");
}

if(t1.toSeconds() != 72) {
	throw new AssertionError("duration toSeconds test failed");
}
