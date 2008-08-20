/* Regression test for JFXC-918 : Referring a non-sequence var within a for expression throws NPE
 *
 * @test
 * @run
 */

import java.lang.System;

class X {
	var x:Number;
	var y = bind for (xi in x) f(xi);
	function f(xi:Number):Number {
		return xi + 10;
	}
}
var ax = X {x: 2.3}
System.out.println(ax.y)
