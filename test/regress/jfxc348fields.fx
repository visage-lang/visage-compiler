/*
 * Regression test: field access within a bind
 *
 * @test
 * @run
 */

import java.lang.System;

var x = bind { System.out.println("hi"); 0 }

System.out.println(x);
