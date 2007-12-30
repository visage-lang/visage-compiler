/*
 * Regression test: NPE in type morpher
 *
 * @test
 * @run
 */

class Foo {
   attribute a : Integer;
}

Foo {
  var bar : Integer = 1;
  a: bar
}

