/*
 * JFXC-4221 : Diamond shaped dependencies cause invalidations to be lost
 *
 * Single eval
 *
 * @test
 * @run
 */

class A {
   var x:Number;
   var y:Number;
}

var invXcnt = 0;
var invYcnt = 0;

function f(x : Number, y : Number) : Number {
  println("f({x}, {y})");
  x + y
}

var factor: Number = bind a.x/a.y on replace { println("factor: {factor}") };
var X = bind factor on invalidate { ++invXcnt };
var Y = bind factor on invalidate { ++invYcnt };
var dummy = bind f(X, Y) on replace {println("dummy: {dummy}");}

println("Invalidate X count: {invXcnt}");
println("Invalidate Y count: {invYcnt}");

println("-- a --");
var a:A = A {
    x:100;
    y:100;
}

println("Invalidate X count: {invXcnt}");
println("Invalidate Y count: {invYcnt}");

println("-- set a.x --");
a.x = 200;

println("Invalidate X count: {invXcnt}");
println("Invalidate Y count: {invYcnt}");

println("-- set a.y --");
a.y = 200;

println("Invalidate X count: {invXcnt}");
println("Invalidate Y count: {invYcnt}");
