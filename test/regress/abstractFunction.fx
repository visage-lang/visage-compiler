/**
 * regression test:  abstract functions and abstract classes
 *
 * @test
 * @run
 */

import java.lang.System;

abstract class Foo {
   abstract function bar() : Integer;
   function baz() : Integer { 33 }
}

class Food extends Foo {
  function bar() : Integer { 22 }
}

var ood = new Food;
System.out.println(ood.bar());
System.out.println(ood.baz());

