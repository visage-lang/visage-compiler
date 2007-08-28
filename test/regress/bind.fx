/*
 * Test bind both in module variables and class attribute.
 * Regression test against them smashing each other.
 * @test
 * @run
 */

import java.lang.System;

public class Bar {
	readonly attribute alpha = 3;
	private attribute beta = bind alpha * 10;
	function foo(x : Integer, y : Integer) { x - y } 
}

var pass = true;

var v1 = 0;
var v2 = bind v1 * 5;

var bb = new Bar;
pass = (if bb.alpha == 3 then pass else false);
pass = (if bb.beta == 30 then pass else false);
System.out.println("{pass} Default-thirty beta={bb.beta}");

v1 = 1;
pass = (if v2 == 5 then pass else false);
System.out.println("{pass} v2-five ={v2}");

bb = Bar { alpha: 21 };
pass = (if bb.alpha == 21 then pass else false);
pass = (if bb.beta == 210 then pass else false);
System.out.println("{pass} Two-Ten beta={bb.beta}");

v1 = 9;
pass = (if v2 == 45 then pass else false);
System.out.println("{pass} v2-forty-five ={v2}");

System.out.println(if pass then "PASS" else "FAIL");
