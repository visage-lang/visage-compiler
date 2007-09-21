/*
 * @test
 * @run
 */

import java.lang.System;

class Foo {
  attribute uno 
    on replace { System.out.println("Changed:{ uno }"); };
  init  { System.out.println("Initialized:{ uno }"); }
}
var x = Foo { uno: 1 };
x.uno = 99;
