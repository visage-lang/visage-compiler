/**
 * regression test: JFXC-940 : Make the generated scope of a local variable declaration 
 * be the entire block in which it is defined. 
 *
 * @compilearg -XDfwdRefError=false
 * @test
 * @run
 */
import java.lang.*;

{ 
  var a = 1; 
  ++a;
  System.out.println("a= {a}");

  var x =  y + 1; 
  var y = 16; 
  ++y; 
  System.out.println("x= {x}"); 
  System.out.println("y= {y}");


  var m = 1;
  var n = bind m;
  m++;
  System.out.println("m = {m} n = {n}");
}




