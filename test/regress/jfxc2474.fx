/**
 * @test
 * @run
 */

import java.lang.System;

var x0 = 2.5;
var x1 : Float = 22.5;
var x2 : Double = 22.5;
var x3 = 2323213;
var x4 = 455345543545435435;
var xs : Object[] = [x0, x1, x2, x3, x4];

System.out.println("x0 = {x0} is boxed as {xs[0].getClass().getName()}");
System.out.println("x1 = {x1} is boxed as {xs[1].getClass().getName()}");
System.out.println("x2 = {x2} is boxed as {xs[2].getClass().getName()}");
System.out.println("x3 = {x3} is boxed as {xs[3].getClass().getName()}");
System.out.println("x4 = {x4} is boxed as {xs[4].getClass().getName()}");
