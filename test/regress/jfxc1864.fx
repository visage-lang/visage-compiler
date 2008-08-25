/**
 * Regression test JFXC-1864 : Use arguments name specified in run(argsName : String[]) function
 *
 * @test
 * @run/param Yoba
 */

import java.lang.System;

function run(myArgs : String[]) {
   System.out.println(myArgs[0]);
}
