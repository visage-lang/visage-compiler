/*
 * Feature test #8 -- change triggers
 * Demonstrates simple change triggers
 *
 * @test
 */

import java.lang.System;

class Foo {
    attribute x : Integer
        on change { System.out.println("x: {x}"); };
    attribute y : Integer
        on change { System.out.println("y: {y}"); };
    attribute z : Integer = 3
        on change { System.out.println("z: {z}"); };
}

var n = 3;
var f = Foo { x: 3, y: bind n+1 };
f.x = 4;
f.x = 4;
n = 10;
