/*
 * Regression test for JFXC-757 : NPE on non-constant override attribute default
 *
 * @test
 * @run
 */

import java.lang.System;

class Guts {
  attribute name : String
}

class Base {
  attribute gut : Guts = Guts {name: "John Doe"}
}

class Sub extends Base {
  override attribute gut = Guts {name: "Jane Doe"}
}

var x = new Sub;
System.out.println(x.gut.name)
