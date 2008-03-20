import java.lang.System;

/*
 * keywords not used in sqe functional tests as of 3/18/08
 * let, readonly, super, this
 *
 * @test
 * @compilefirst ../TestUtils.fx
 * @run
 */
var TU = new TestUtils;

 /*
  * let
  * way of defining final variable
  */
let x = 10;
/* Should this be a compile error or just retain original value and ignore attempt
 * to assign.
 */
TU.checkI(x,10,"variable defined with let cannot be assigned too.");

//this should fail to compile..I think
x=20;  
//hopefully one of these lines will begin to fail
TU.checkI(x,20,"variable defined with let cannot be assigned too.");

protected class A {
public readonly attribute a = 32;
public function getA():Integer { return a; }
public function getAThis():String { 
  var sthis= this.toString(); 
  return sthis; 
  }
}
var a1 = new A;

//this should fail
a1.a = 42; 
//..or this should fail, hopefully, as with let, one of these or both will fail
TU.checkI(a1.a, 42, "readonly attribute should not be able to be reassigned.");

protected class B extends A {
  readonly attribute b = 33;
  function getBb():Integer { return b; }
  function getBa():Integer { var a = super.getA(); return a; }
  function getBThis():String { var bst = this.toString(); return bst; }
  function getSuperThis():String { var st = super.getAThis(); return st; }
}

/*
 * check calls to super class method and method through 'super'
 */
var b1 = new B;
TU.checkI(b1.getBa(),32,"super class method call");
TU.checkI(b1.getA(),32,"super class attribute");

/*
 * Not sure of these. 
 * Checks String representation of this in class and super class
 */
TU.checkB( a1.getAThis().startsWith("keywords02$A"),"check return of this");
TU.checkB( b1.getSuperThis().startsWith("keywords02$B"),"check return of super this");

class C extends B {
 readonly attribute y = 34;
 attribute y2 = 35;
 function getCBa(): Integer { var _a = super.getBa(); return _a; }
 function getCThis():String { var _this = this.toString(); return _this; }
 function getSuperSuperThis():String { return super.getSuperThis(); }
}

/*
 * classes for 'this' checks
 */
abstract class inner {
	readonly attribute innerthis;
	abstract function getThis():String 
 }
abstract class localclass {
	readonly attribute localthis;
	abstract function getThis():String 
}

class outer {
 readonly attribute outterthis = this;
 function getThis():String { 	var sthis = outterthis.toString(); 	return sthis;  }

 attribute inn = inner {
	function getThis():String { var sthis = this.toString(); return sthis; }
 }


 function mlocal():String {
	var m_local = localclass { 
	       function getThis():String { var sthis = this.toString(); return sthis; }
	}
	return m_local.getThis();
 }
}

var o = new outer;
/*
 * more dubious check of string representation of various 'this' objects:
 *   outer this(normal), anonymouse inner this, anonymous local this
 */ 
TU.checkB(o.getThis().startsWith("keywords02$outer"),"check outer class this");
TU.checkB(o.inn.getThis().startsWith("keywords02$outer$1inner$anon1"),"check this of anon. inner class");
TU.checkB(o.mlocal().startsWith("keywords02$outer$1localclass$anon2"),"check this of local anon class");

//create a C for tests
var c = new C;
/*
 * checks using 'super' to call methods returning 'this' 
 * check 'this' strings for class and subclasses
 */ 
var these = [ c.getCThis(),"keywords02$C" , c.getSuperSuperThis(),"keywords02$C",  b1.getBThis(),"keywords02$B" , b1.getSuperThis(),"keywords02$B" , a1.getAThis(),"keywords02$A"];
for( i in [0..8 step 2]){TU.checkB( these[i].startsWith(these[i+1]) , "check sequense of this values");}

/*
 * retrieve value of parent class via calls to super class methods
 */
TU.checkI(c.getCBa(), 32, "call to super's method calling super.");

/* 
 * c.y is a readonly attribute.
 * Should this be a compile error or just retain original value and ignore attempt
 * to assign. 
 */
c.y = 25;//should fail?
TU.checkI(c.y,25,"Class variable defined as readonly cannot be assigned too.");


TU.report();