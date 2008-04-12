/* Regression test for JFXC-1085 : object literal with extension referencing enclosing class
 *
 * @test
 * @run
 */

import java.lang.System;

class Foo {
  attribute y : Integer
}

class Bar {
  attribute rat = 8;
  function yum() : Foo {
    Foo {
     override attribute y = rat;
    }
  }
}

var nb = Bar {}
System.out.println(nb.yum().y)

