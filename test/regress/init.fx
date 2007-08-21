/*
 * @test
 */

import java.lang.System;

class Foo {
  attribute uno 
    on change { System.out.println("Changed:{ uno }"); };
  init  { System.out.println("Initialized:{ uno }"); }
}
var x = Foo { uno: 1 };
x.uno = 99;
