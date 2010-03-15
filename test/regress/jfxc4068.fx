/*
 * JFXC-4068 : PrimitiveShapes - transition from LinearGradient to RadialGradient
 *
 * Original non-GUI
 *
 * @test
 * @run
 */

class A {
   var name:String;
   var x:Number;
}

var a1:A =  bind A {
   name: "A1"
   x: variable
}

var a2:A =  bind A {
   name:"A2"
   x: variable
}

var variable = 0;
var flag = true;

var a:A = bind if (flag) a1 else a2 on replace { println("on-replace  a.name: {a.name}, a.x: {a.x}"); }
//var a:A = bind a1 on replace {...} // this works

println("START");
flag = false;
for (x in [0..10]) {
   println("x = {x}");
   variable = x;
}

