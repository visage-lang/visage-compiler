/*
 * @test
 * @run/fail
 */

import java.lang.System; 

public class T { 
  attribute x: Number = 0 
    on replace {System.out.println("x={x}");} 
  attribute y: Number = bind x 
    on replace {System.out.println("y={y}");} 
  init { 
    x = 20; 
  } 
} 

var t = T { 
  x: 100 
}; 
