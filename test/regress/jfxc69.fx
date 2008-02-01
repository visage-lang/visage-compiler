/**
 * regression test:  Default value for String is "", not null.
 * @test
 * @run
 */

import java.lang.System;

var b : String;

class Foo {
    attribute a : String;
}

var foo = new Foo;

System.out.println(foo.a);
System.out.println(b);
