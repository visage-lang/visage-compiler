/**
 * Regression test JFXC-1250 : visibility modifiers
 *
 *     script-level function call
 *
 * @compilefirst jfxc1250/a/jfxc1250subFailA.fx
 * @compilefirst jfxc1250subFail.fx
 * @test/compile-error
 */

import java.lang.System;
import jfxc1250.a.jfxc1250subFailA;

public function slHereFPub() { 3 }
package function slHereFPkg() { 3 }
function slHereFScr() { 3 }

class Inh extends jfxc1250subFail {

    function slCall() {
	slFPub();   // inherited, same package
	slFPkg();   // inherited, same package
	slFScr();   // ERROR: inherited, same package
    }

    function slHereCall() {
	slHereFPub();   // nested, access to script
	slHereFPkg();   // nested, access to script
	slHereFScr();   // nested, access to script
    }
}

class InhA extends jfxc1250subFailA {

    function slCall() {
	slFPub();   // inherited, different package
	slFPkg();   // ERROR: inherited, different package
	slFScr();   // ERROR: inherited, different package
   }
}

function slInhCall() {
	var vf = Inh {};
	vf.slCall();
	vf.slHereCall();
}

function slInhACall() {
	var vf = InhA {};
	vf.slCall();
}

function slCall() {
	System.out.println( slHereFPub() );   // this script class, direct access
	System.out.println( slHereFPkg() );   // this script class, direct access
	System.out.println( slHereFScr() );   // this script class, direct access
}

function slHereCall() {
	System.out.println( jfxc1250slCall.slHereFPub() );   // this script class, select access
	System.out.println( jfxc1250slCall.slHereFPkg() );   // this script class, select access
	System.out.println( jfxc1250slCall.slHereFScr() );   // this script class, select access
}

function slSubCall() {
	System.out.println( jfxc1250subFail.slFPub() );   // same package
	System.out.println( jfxc1250subFail.slFPkg() );   // same package
	System.out.println( jfxc1250subFail.slFScr() );   // ERROR: same package
}

function slSubACall() {
	System.out.println( jfxc1250subFailA.slFPub() );   // different package
	System.out.println( jfxc1250subFailA.slFPkg() );   // ERROR: different package
	System.out.println( jfxc1250subFailA.slFScr() );   // ERROR: different package
}

function run( ) {
	slInhCall();
	slInhACall();
	slCall();
	slHereCall();
	slSubCall();
	slSubACall();
}