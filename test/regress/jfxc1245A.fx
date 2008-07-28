/**
 * Regression test for JFXC-1245 : Override keyword for functions
 *
 * @compilefirst jfxc1245Asub.fx
 * @compilefirst jfxc1245Asub2.fx
 * @test
 * @run
 */

import java.lang.System;

def x = jfxc1245Asub2{}
System.out.println(x.foo());

