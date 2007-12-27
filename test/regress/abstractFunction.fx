/**
 * regression test:  abstract functions and abstract classes
 *
 * @test/fxunit
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

public class abstractFunction extends junit.framework.TestCase {
    function test() {
        var ood = new Food;
        assertEquals(22, ood.bar());
        assertEquals(33, ood.baz());
    }
}