/*
 * Regression test for JFXC-3649 - Compiled bind: binds should work across mixee to mixin.
 *
 * @test
 * @run
 */

mixin class M {
  var mx = [1, 2, 3];
  var my = 1;
}

class A extends M {
  var ax = bind mx;
  var ay = bind my;
}

var a = A{};

a.mx[1] = 10;
println("{a.ax}");

a.mx[1] = 20;
println("{a.ax}");

a.my = 10;
println("{a.ay}");

a.my = 20;
println("{a.ay}");
