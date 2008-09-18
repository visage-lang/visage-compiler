/*
 * JFXC-1955 :Compiler error while creating a sequence of functions in bind-context
 * 
 * @test/fail
 * 
 *
 */

import java.lang.System;
import java.lang.Math;

function calc(arg:Number):function(a:Number):Number {
    function(arg:Number):Number {
             var radians = Math.toRadians(arg);
             return Math.sin(radians);
     };
}


var fvSeq=bind for(i in [1..5]) calc(i);
var i=5;
for(fn in fvSeq){
  System.out.println("{fn(2*i++)}");
  
} 