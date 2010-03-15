/**
 * Regression test JFXC-4195 : EffectsPlaygroundMobile: Flip Vertical moves the image too high
 *
 *  @test
 *  @run
 */

class A {
   var x:Number;
   var y:Number;
}

var factor: Number = bind a.x/a.y on replace {println("factor is {factor}")};
var X = bind lazy 100*factor on invalidate {println("invalidating X");};
var Y = bind lazy 100*factor on invalidate {println("invalidating Y");};
var dummy:A[] = bind A{x:X, y:Y} on replace {println("dump [x = {dummy[0].x}, y = {dummy[0].y}]");}

var a:A = A {
    x:100;
    y:100;
}

X;
Y;

a.x = 200;
a.y = 200;
