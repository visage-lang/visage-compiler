/*
 * Regression test: Characters like #,$,%.. followed by {} in string expression declaration throws VerifyError
 *
 * @test
 * @run
 */

import javafx.ui.*;
var j = 20;
var i:String ="{i} #"; 
java.lang.System.out.println(i);
