/*
 * @test
 * @run
 */
import java.lang.System;
import javafx.lang.FX;

var version: String = FX.getProperty("javafx.version");
var ok = true;
if (version == "unknown")
    ok = false;
System.out.println(if (ok) "PASS!" else "FAIL!");
	
