/*
 *
 * JFXC-1902: bind with inverse inside object literals doesn't sync up values
 * A bidirectional bind of two instance variables at object creation time (i.e.,inside object literal).
 * @test
 * @run/fail
 * 
 */

import java.lang.System;

class jfxc1902{
    protected var x=10;
    protected var y=5;
}

public var ref:jfxc1902=jfxc1902{
     x:10;
     y:bind ref.x with inverse; 
 }
 

function run(args:String[]){
    System.out.println("x={ref.x}");
    System.out.println("y={ref.y}");
    ref.x=30;
    System.out.println("x={ref.x}");
    System.out.println("y={ref.y}");
 } 