/*
 * Duration_TS005_01.fx

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


var t1: Duration = 1m;
var t2: Duration = Duration{ millis: 10000 };

if(t1.add(t2).sub(t2).div(2) != 30s) {
	throw new AssertionError("duration test failed");
}

if(not t1.add(t2).sub(t2).equals(t1)) {
	throw new AssertionError("duration test failed");
} 
