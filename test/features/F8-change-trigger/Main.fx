/*
 * Feature test #8 -- replace triggers
 * Demonstrates simple replace triggers
 *
 * @test
 * @run
 */

import java.lang.System;

class Foo {
    attribute x : Integer
        on replace { System.out.println("x: {x}"); };
    attribute y : Integer
        on replace { System.out.println("y: {y}"); };
    attribute z : String = "Ralph"
        on replace { System.out.println("z: {z}"); };
}

var n = 3;
var f = Foo { x: 3, y: bind n+1 };
f.x = 4;
f.x = 4;
n = 10;

class Foo2 {
    attribute x : Integer
        on replace (oldValue) { System.out.println("x: {oldValue} => {x}"); };
    attribute y : Integer
        on replace (oldValue) { System.out.println("y: {oldValue} => {y}"); };
    attribute z : String = "Bert"
        on replace (oldValue) { System.out.println("z: {oldValue} => {z}"); };
}

var n2 = 3;
var f2 = Foo2 { x: 3, y: bind n2+1 };
f2.x = 4;
f2.x = 4;
n2 = 10;
f2.z = "Zoey"

