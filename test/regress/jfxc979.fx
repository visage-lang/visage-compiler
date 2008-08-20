/**
 * Regression test: bind to a value that has been set during initialization.
 *
 * @test
 * @run
 */

import java.lang.System;

class Foo {
    var a : Integer on replace {
        b = a;
    }
     
    var b : Integer;
}

var x = 10;
var f = Foo{b: bind x};
System.out.println(f.b);
x=20;
System.out.println(f.b);
