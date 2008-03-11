import java.lang.System;
/*
 * @test
 * @compilefirst ../../TestUtils.fx
 * @run
*/

class T extends TestUtils {
public attribute b:Boolean	on replace { GFT++; System.out.println("Boolean new value: {b}"); }
public attribute s:String	on replace oldvalue  { GFT++; System.out.println("String old:{oldvalue}  new:{s}"); }
public attribute i:Integer	on replace oldvalue=newvalue  { GFT++; System.out.println("Integer old:{oldvalue}  newvalue:{newvalue}==i:{i}"); }

function test() {
   System.out.println("-----set Boolean 5 times; false, true, true,false, false-----");
	b=false;
	b=true;
	b=true;
	b=false;
	b=false;

   System.out.println("-----set Integer 5 times; first to 0, 500, 500,0, 0 -----");
	i=0;
	i=500;
	i=500;
	i=0;
	i=0;

	System.out.println("-----set String 5 times; first to Hello, Hello, \"\",\"\"-----");
	s="";
	s="Hello";
	s="Hello";
	s="";
	s="";
}
}


System.out.println("-----all triggers fire at creation, even to default values------");
var t = new T;
System.out.println("-----triggers do not fire unless value changes------");
t.test();
t.report();
