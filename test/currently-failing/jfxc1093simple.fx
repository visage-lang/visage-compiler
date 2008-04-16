/*
 * Regression test JFXC-1087 : bound interpolator, et. al.
 *
 * @test/fail
 */

class Foo {
  attribute x : Integer
}

var y = [77, 88];
var idx = 0;
Foo {
  x: bind y[idx] with inverse
}
