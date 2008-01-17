/*
 * @test
 * @run
 */

import java.lang.System;

class Foo { 
  attribute x : Integer[] = [ 0 ]
    on insert [ndx] (f) { 
      System.out.println(f); 
    };
  attribute y : Integer
    on replace { 
      System.out.println("y: {y}");
    };
} 

var v = Foo { x : [1, 2, 3] }; 
v.x = [4, 5, 6]; 
var w = Foo { y: 3 };
