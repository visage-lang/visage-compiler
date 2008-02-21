/**
 * Test general init processing in advance of full 710 implementation
 * @test
 * @run
 */

import java.lang.System;
import java.util.BitSet;

class A {
   attribute ai = 1;
   static attribute asx = 2;
   function afi() : Integer { 3 }
   static function afs() : Integer { 4 }
}

class B extends A {
   attribute bi = 1;
   static attribute bs = 2;
   function bfi() : Integer { 3 }
   static function bfs() : Integer { 4 }
}

abstract class C extends B {
   attribute ci = 1;
   static attribute cs = asx;
   function cfi() : Integer { 3 }
   static function cfs() : Integer { 4 }
   abstract function show() : Void;
}

var x = C {
   attribute di = 1;
   static attribute ds = 2;
   function dfi() : Integer { 3 }
   static function dfs() : Integer { 4 }
   function show() : Void {
	System.out.println( di );
	System.out.println( ds );
	System.out.println( dfi() );
	System.out.println( dfs() );
   }
};
System.out.println( x.ai );
System.out.println( x.asx );
System.out.println( x.afi() );
System.out.println( x.afs() );
System.out.println( x.bi );
System.out.println( x.bs );
System.out.println( x.bfi() );
System.out.println( x.bfs() );
System.out.println( x.ci );
System.out.println( x.cs );
System.out.println( x.cfi() );
System.out.println( x.cfs() );
x.show();

class E extends BitSet, C {
   attribute ei = 1;
   static attribute es = 2;
   function efi() : Integer { 3 }
   static function efs() : Integer { 4 }
   function show() : Void {System.out.println( cardinality() );} 
}
var y = new E;
System.out.println( y.ai );
System.out.println( y.asx );
System.out.println( y.afi() );
System.out.println( y.afs() );
System.out.println( y.bi );
System.out.println( y.bs );
System.out.println( y.bfi() );
System.out.println( y.bfs() );
System.out.println( y.ci );
System.out.println( y.cs );
System.out.println( y.cfi() );
System.out.println( y.cfs() );
System.out.println( y.ei );
System.out.println( y.es );
System.out.println( y.efi() );
System.out.println( y.efs() );
y.flip(9);
y.show();







