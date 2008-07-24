/*
 * Regression test: allow local vars declared in an object literal to be visible to the rest of the object literal
 *
 * @test
 * @run
 */

import java.lang.System;

public abstract class X {
    public attribute a: Number = 1;
    public attribute b: Number = 2;
    public abstract function givemec() : Integer;
    public abstract function f(x : Number) : Number;
    public abstract function increment() : Void;
}

var x = X {
    var q = 100
    a: q
    b: q + 10
    public function givemec() : Integer { q * q }
    public function f(x : Number) { x * q }
    public function increment() : Void { ++q };
}

System.out.println(x.a);
System.out.println(x.b);
System.out.println(x.givemec());
System.out.println(x.f(5));
x.increment();
System.out.println(x.f(5));


