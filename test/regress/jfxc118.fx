/*
 * Regression test: allow local vars declared in an object literal to be visible to the rest of the object literal
 *
 * currently disabled
 */

import java.lang.System;

public class X {
    public attribute a: Number = 1;
    public attribute b: Number = 2;
}

X {
    var q = 100
    a: q
    b: q + 10
}
