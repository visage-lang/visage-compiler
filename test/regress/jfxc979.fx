/**
 * Regression test: bind to a value that has been set during initialization.
 *
 * @test
 * @run
 */

import java.lang.System;

class Foo {
    attribute a : Integer on replace {
        b = a;
    }
     
    attribute b : Integer;
}

var x = 10;
var f = Foo{b: bind x};
System.out.println(f.b);
x=20;
System.out.println(f.b);
