/**
 * regression test:  unary operators don't destroy the type of a variable.
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
