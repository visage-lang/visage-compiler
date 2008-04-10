/* Regression test for JFXC-809 : Triggers
 *
 * @test
 * @run
 */

import java.lang.System;

public class T {
  attribute x: Number = 0
      on replace {System.out.println("x ={x}");}
  attribute y: Number = bind x
      on replace {System.out.println("y ={y}");}
  attribute zx: Number = bind x
      on replace {System.out.println("zx ={zx}");}
  attribute zy: Number = bind y
      on replace {System.out.println("zy ={zy}");}
  attribute zy2: Number = bind y
      on replace {System.out.println("zy2 ={zy2}");}
  init {
      x = 20;
  }
}

System.out.println("A--------");

var t = T {};

System.out.println("B--------");
t = T { x: 1};

System.out.println("C--------");
t.x=100;

System.out.println("D--------");
var u = bind t.x
   on replace {System.out.println("ux ={u}");} //this does not print when t.x is changed

t.x = 200;

