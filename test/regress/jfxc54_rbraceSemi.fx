/*
 * @test
 * @run
 */

import java.lang.System;

class Foo {
  attribute hop;
  public function foo() { 55 }
};

var h = { 44 + 2 }
if (h > 4) then { 
  System.out.println("Greater")
} else {
  System.out.println("Less")
};
if (h < 100) then { 
  System.out.println("That too"); null
}
var c : Integer[] = foreach (x in [1..10]) { x*x }
System.out.println(c);
