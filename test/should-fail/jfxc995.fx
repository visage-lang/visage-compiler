/*
 * @test/compile-error
 */

class Bar extends Foo {
  function x() {
    System.out.println("BAR");
  }
}
class Foo extends Bar,One {
  function x() {
    System.out.println("Foo");
  }
}
class One extends Bar {
}
