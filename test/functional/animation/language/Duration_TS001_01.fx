/*
 * Duration_TS001_01.fx

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


var t1: Duration = 1m + 12s;
var t2: Duration = 1.2m;
var t3 = 72000ms;

if(t1 != t2 or t2 != t3 or t3 != t1) {
	throw new AssertionError("duration test failed");
}
