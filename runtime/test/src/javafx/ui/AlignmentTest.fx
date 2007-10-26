/*
 * Test javafx.ui.Alignment class
 * @test
 * #run
 */

import javafx.ui.Alignment;
import java.lang.System;

var trailing = Alignment.TRAILING;
var leading = javafx.ui.Alignment.LEADING;
var center = javafx.ui.Alignment.CENTER;
var baseline = javafx.ui.Alignment.BASELINE;

System.out.println(leading.name);  // LEADING
System.out.println(trailing.name); // TRAILING
System.out.println(center.name);   // CENTER
System.out.println(baseline.name); // BASELINE
 
