/**
 * Test for JFXC-529: double value can not be subtracted from long.
 *
 * @test/fail
 */

import java.lang.*;

var t = 23.4;
var k = Math.round(t/12.1);
System.out.println(t-k); 
