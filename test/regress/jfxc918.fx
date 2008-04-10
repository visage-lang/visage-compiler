/* Regression test for JFXC-918 : Referring a non-sequence attribute within a for expression throws NPE
 *
 * @test
 * @run
 */

import java.lang.System;

class X {
	attribute x:Number;
	attribute y = bind for (xi in x) f(xi);
	function f(xi:Number):Number {
		return xi + 10;
	}
}
var ax = X {x: 2.3}
System.out.println(ax.y)
