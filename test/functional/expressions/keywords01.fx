import java.lang.System;
import java.lang.Exception;

/*
 * abstract class, function, bind used with readonly
 * @test
 * @compilefirst ../TestUtils.fx
 * @run
 */
var TU = new TestUtils;

abstract class foo {
 abstract bound function RequiredAbstractFunction():Integer;
 function CanHaveNonAbstractFunctionsInAbstractClass() {}
}

public class foo2 extends foo {
	private attribute data:Integer = 10;
	public readonly attribute roI = bind data;
	public readonly attribute roS = "readonly";

	bound function RequiredAbstractFunction():Integer { return data; }
	function setData( newd : Integer) { data = newd;}
	function roi():Integer {return roI; }
	function ros():String { return roS; }
 }
var v = 10;
var f2 = new foo2;
TU.checkI(f2.roi(),10,"check bound readonly attribute");

//fb2 is bound to foo2.data + v
let fb2 = bind f2.RequiredAbstractFunction() + v;
TU.checkI(fb2, 20,"var bound to function in expression");
f2.roi();

function checkfoo( i:Integer, i2:Integer):Integer {
   var ret = 1;
	if(i != i2) { 
		throw new Exception("{i}!={i2}"); 
	} else {  
		ret = 0; 
	}
	return ret;
}

for(i in [10,20]) {
	v = i;
	f2.setData(i);
	TU.checkI(f2.roi(),i,"check bound readonly attribute");
	try {
		checkfoo(fb2,i+v); //i+v since setData uses i and fb2 is bound to v :)
	} catch(e:Exception) {
		TU.checkB(e instanceof com.sun.javafx.runtime.BindingException,
				"check BindingExecption1: " + e);
	} finally {
	   TU.check(true,"Make sure we got to finally block");
	}
}

f2.setData(50);
f2.roi();
try {
	f2.roI = 20;
} catch( be: Exception ) {
	TU.checkB(be instanceof com.sun.javafx.runtime.BindingException,
		"check BindingExecption2: " + be);
}
if( f2.ros().compareTo("readonly")!=0){
	throw new Exception("failed on check of initial value of readonly string"); 
}
f2.roS = "Hello, World!";
TU.checkS(f2.ros(),"Hello, World!","check value of readonly string after reasssignment!");

/*
 * JFXC-36 Cannot assign to a readonly attribute from within same class
 * readonly keyword semanctics not implemented.
 */
class ro {
	readonly attribute ROA = 10;
	function roa():Integer { return ROA; }
}
var r = new ro;
if(r.roa()!=10){ 
	throw new Exception("initial value of ROA is incorrect!");
}
r.ROA=100;  //<--should not be allowed???
TU.checkI(r.roa(),100,
	"Why can this readonly variable be set? If you're reading this, it has been fixed!");

TU.report();
