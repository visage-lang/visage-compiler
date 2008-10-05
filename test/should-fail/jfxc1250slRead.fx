/**
 * Regression test JFXC-1250 : visibility modifiers
 *
 * Read component.
 *
 * @compilefirst jfxc1250/a/jfxc1250subFailA.fx
 * @compilefirst jfxc1250subFail.fx
 * @test/compile-error
 */
import java.lang.System;
import jfxc1250.a.jfxc1250subFailA;

public var slHerePub = 0;
package var slHerePkg = 0;
var slHereScr = 0;

public-read package var slHereRPkg = 0;
public-read var slHereRScr = 0;

class Inh extends jfxc1250subFail {

    function slRead() {
	System.out.println( slPub );   // inherited, same package
	System.out.println( slPkg );   // inherited, same package
	System.out.println( slScr );   // ERROR: inherited, same package

	System.out.println( slRPkg );   // inherited, same package
	System.out.println( slRScr );   // inherited, same package
    }

    function slHereRead() {
	System.out.println( slHerePub );   // nested, access to script
	System.out.println( slHerePkg );   // nested, access to script
	System.out.println( slHereScr );   // nested, access to script

	System.out.println( slHereRPkg );   // nested, access to script
	System.out.println( slHereRScr );   // nested, access to script
    }
}

class InhA extends jfxc1250subFailA {
    function slRead() {
	System.out.println( slPub );   // inherited, different package
	System.out.println( slPkg );   // ERROR: inherited, different package
	System.out.println( slScr );   // ERROR: inherited, different package

	System.out.println( slRPkg );   // inherited, different package
	System.out.println( slRScr );   // inherited, different package
    }
}

function slInhRead() {
	var vf = Inh {};
	vf.slRead();
	vf.slHereRead();
}

function slInhARead() {
	var vf = InhA {};
	vf.slRead();
}

function slRead() {
	System.out.println( slHerePub );   // this script class, direct access
	System.out.println( slHerePkg );   // this script class, direct access
	System.out.println( slHereScr );   // this script class, direct access

	System.out.println( slHereRPkg );   // this script class, direct access
	System.out.println( slHereRScr );   // this script class, direct access
}

function slHereRead() {
	System.out.println( jfxc1250slRead.slHerePub );   // this script class, select access
	System.out.println( jfxc1250slRead.slHerePkg );   // this script class, select access
	System.out.println( jfxc1250slRead.slHereScr );   // this script class, select access

	System.out.println( jfxc1250slRead.slHereRPkg );   // this script class, select access
	System.out.println( jfxc1250slRead.slHereRScr );   // this script class, select access
}

function slSubRead() {
	System.out.println( jfxc1250subFail.slPub );   // same package
	System.out.println( jfxc1250subFail.slPkg );   // same package
	System.out.println( jfxc1250subFail.slScr );   // ERROR: same package

	System.out.println( jfxc1250subFail.slRPkg );   // same package
	System.out.println( jfxc1250subFail.slRScr );   // same package
}

function slSubARead() {
	System.out.println( jfxc1250subFailA.slPub );   // different package
	System.out.println( jfxc1250subFailA.slPkg );   // ERROR: different package
	System.out.println( jfxc1250subFailA.slScr );   // ERROR: different package

	System.out.println( jfxc1250subFailA.slRPkg );   // different package
	System.out.println( jfxc1250subFailA.slRScr );   // different package
}

function run( ) {
	slInhRead();
	slInhARead();
	slRead();
	slHereRead();
	slSubRead();
	slSubARead();
}
