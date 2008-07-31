/**
 * Test general init processing in advance of full 710 implementation
 * @test
 * @compilefirst jfxc710WayOut.fx
 * @compilefirst jfxc710Out.fx
 * @run
 */

import java.lang.System;
import java.util.BitSet;

class A extends jfxc710Out {
   override var outi = 'ovA-outi'
       on replace { System.out.println('trig: override A out1 {outi}') }
   override var wayOuti = 'ovA-wayOuti'
       on replace { System.out.println('trig: override A wayOuti {wayOuti}') }
   var ai = 'ai'
       on replace { System.out.println('trig: a1 {ai}') }
   function afi() : String { 'afi' }
   function show() : Void {
	super.show();
	System.out.println( ai );
	System.out.println( afi() );
   }
}

class B extends A {
   override var ai = 'ovB-ai'
       on replace { System.out.println('trig: override B a1 {ai}') }
   var bi = 'bi';
   function bfi() : String { 'bfi' }
   function show() : Void {
	super.show();
	System.out.println( bi );
	System.out.println( bfi() );
   }
}

abstract class C extends B {
   var ci = 'ci';
   function cfi() : String { 'cfi' }
   abstract function cshow() : Void;
   function show() : Void {
	super.show();
	System.out.println( ci );
	System.out.println( cfi() );
   }
}

System.out.println("------- WayOut\{\}");
var x = jfxc710WayOut{};
x.show();

System.out.println("------- WayOut\{wayOuti: 'litWayOuti'\}");
x = jfxc710WayOut{wayOuti: 'litWayOuti'};
x.show();

System.out.println("------- Out\{\}");
x = jfxc710Out{};
x.show();

System.out.println("------- Out\{wayOuti: 'litOutWayOuti'\}");
x = jfxc710Out{wayOuti: 'litOutWayOuti'};
x.show();

System.out.println("------- Out\{outi: 'litOuti'\}");
x = jfxc710Out{outi: 'litOuti'};
x.show();

System.out.println("------- A\{\}");
x = A{};
x.show();

System.out.println("------- A\{wayOuti: 'litAWayOuti'\}");
x = A{wayOuti: 'litAWayOuti'};
x.show();

System.out.println("------- A\{outi: 'litAOuti'\}");
x = A{outi: 'litAOuti'};
x.show();

System.out.println("------- A\{ai: 'litAi'\}");
x = A{ai: 'litAi'};
x.show();

System.out.println("------- B\{\}");
x = B{};
x.show();

System.out.println("------- B\{wayOuti: 'litBWayOuti'\}");
x = B{wayOuti: 'litBWayOuti'};
x.show();

System.out.println("------- B\{outi: 'litBOuti'\}");
x = B{outi: 'litBOuti'};
x.show();

System.out.println("------- B\{ai: 'litBAi'\}");
x = B{ai: 'litBAi'};
x.show();

System.out.println("------- B\{bi: 'litBi'\}");
x = B{bi: 'litBi'};
x.show();

System.out.println("------- B-Anon\{\}");
x = B{
   override var ai = 'B-AnonOvAi'
       on replace { System.out.println('trig: override B-Anon ai {ai}') }
};
x.show();


System.out.println("------- C-Anon\{\}");
var xd = C {
   override var ci = 'ovCAnon-ci'
       on replace { System.out.println('trig: override CAnon ci {ci}') }
   function dfi() : String { 'Anonfi' }
   function cshow() : Void {
	show();
	System.out.println( 'Anoni' ); // can be removed
	System.out.println( dfi() );
   }
};

xd.cshow();


System.out.println("------- E-BitSet\{\}");
class E extends C, BitSet {
   var ei = 'ei';
   function efi() : String { 'efi' }
   function cshow() : Void {
	show();
	System.out.println( cardinality() );
	System.out.println( ei );
	System.out.println( efi() );
   }
}
var xe = new E;
xe.flip(9);

xe.cshow();

System.out.println("------- E\{bi: 'litEBi'\}");
xe = E{bi: 'litEBi'};
xe.cshow();

