/**
 * Should-fail test JFXC-2278 : Attribution should catch override of instance var when no 'override' keyword
 *
 * @test/compile-error
 */

class A {
  var x;
  function y() {};
}

class B extends A {
  var x;
  def y = 6;
}
