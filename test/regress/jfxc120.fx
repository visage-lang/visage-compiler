/*
 * Regression test: attributes and functions in "new"
 *
 * @test
 * @run
 */

import java.lang.System;

public abstract class X {
   abstract function blah() : Integer;
}


var xx = X {
   function blah() { 77 * 2 }
};

System.out.println("blah: {xx.blah()}");
