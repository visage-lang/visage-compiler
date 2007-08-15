/*
 * @test
 */

import java.lang.System;

class Foo {
  attribute uno 
    on change { System.out.println("Changed"); };
  init  { System.out.println("Initialized"); }
}
var x = Foo { uno: 1 };
x.uno = 99;

