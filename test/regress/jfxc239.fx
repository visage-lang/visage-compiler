/*
 * Regression test: jfxc204
 *
 * @test
 * @run
 */

import java.lang.*;

var rand = 21;
var s = if (rand % 2 == 0) "s" else 1; 

System.out.println("s: {s}");
