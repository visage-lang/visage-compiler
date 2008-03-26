/**
 * Regression test: bind to a value that has been set during initialization.
 *
 * @test
 * @run
 */

import java.lang.System;

class Foo {
    public attribute a on replace {
        b = a;
    }
     
    public attribute b;
}

var x = 10;
var f = Foo{b: bind x};
System.out.println(f.b);
x=20;
System.out.println(f.b);
