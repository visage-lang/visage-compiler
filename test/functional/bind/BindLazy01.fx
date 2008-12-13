import java.lang.System;

/** A simple test of "bind lazy".
  * @test
  * @run
  */ 

var x : String = "a";
function twice(x:String) {
  System.out.println("twice({x}) called.");
  "{x}{x}"
}
var y : String = bind lazy twice(x);
System.out.println("x is {x}.");
System.out.println("y is {y}.");
x = "b";
System.out.println("x is {x}.");
System.out.println("y is {y}.");
var z = bind twice(y);
System.out.println("x is {x}.");
System.out.println("y is {y}.");
System.out.println("z is {z}.");
x = "c";
System.out.println("x is {x}.");
System.out.println("y is {y}.");
System.out.println("z is {z}.");
