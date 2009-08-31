/**
 * Regression test for JFXC-3174 : Regression: compiler chooses wrong method signature with static import
 *
 * @test
 * @run
 */

import java.lang.Math.*;

var f1:Number = 100;
var f2:Number = 10.5;

var d1:Double = 100;
var d2:Double = 10.5;

var i1:Integer = 100;
var i2:Integer = 10.5;

var x1 = 100;
var x2 = 10.5;


println(java.lang.Math.min(f1, f2) == min(f1, f2));
println(java.lang.Math.min(d1, d2) == min(d1, d2));
println(java.lang.Math.min(i1, i2) == min(i1, i2));
println(java.lang.Math.min(x1, x2) == min(x1, x2));
