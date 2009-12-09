/*
 * Regression test for JFXC-3647 :Compiled bind: override should work for bound and unbound sequences.
 *
 * @test
 * @run
 */

mixin class M {
  var mb = bind [1, 2];
  var mu = [3, 4];
}

class A extends M {
  var ab = bind [5, 6];
  var au = [7, 8];
}

class B extends A {
  override var mb = [11, 12];
  override var mu = [13, 14];
  override var ab = [15, 16];
  override var au = [17, 18];
}

class C extends A {
  override var mb = bind [21, 22];
  override var mu = bind [23, 24];
  override var ab = bind [25, 26];
  override var au = bind [27, 28];
}

var a = A {};

println("a.mb: {a.mb}");
println("a.mu: {a.mu}");
println("a.ab: {a.ab}");
println("a.au: {a.au}");

var b = B {};

println("b.mb: {b.mb}");
println("b.mu: {b.mu}");
println("b.ab: {b.ab}");
println("b.au: {b.au}");

var c = C {};

println("c.mb: {c.mb}");
println("c.mu: {c.mu}");
println("c.ab: {c.ab}");
println("c.au: {c.au}");
