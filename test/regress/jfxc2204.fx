/**
 * Regression test JFXC-2204 : On assignment, object literal init, or initial value, convert literal null to default value
 *
 * @test
 * @run
 */

class A {
  var s : String;
}

function run() {
  var a : String = null;
  println("init: {a}");
  var b = "hi";
  b = null;
  println("assign: {b}");
  var f : String;
  println("default: {f}");

  var g = new A;
  println("instance default: {g.s}");
  g.s = null;
  println("assign instance var: {g.s}");
  g = A {s: null}
  println("object literal: {g.s}");
}