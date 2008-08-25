/**
 * regression test: JFXC-1507 : Unable to use a local variable in triggers
 *
 * @test
 * @run
 */

import java.lang.System;

var a = 10.0;

var b = 2.0 on replace { a = b / 2}; 

System.out.println ("a: {a}");
System.out.println ("b: {b}");

