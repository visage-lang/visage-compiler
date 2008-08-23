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

public-init public var slHereIPub = 0;
public-init package var slHereIPkg = 0;
public-init var slHereIScr = 0;

public-read public var slHereRPub = 0;
public-read package var slHereRPkg = 0;
public-read var slHereRScr = 0;

public-init public-read public var slHereIRPub = 0;
public-init public-read package var slHereIRPkg = 0;
public-init public-read var slHereIRScr = 0;

class Inh extends jfxc1250subFail {

    function slRead() {
	System.out.println( slPub );   // inherited, same package
	System.out.println( slPkg );   // inherited, same package
	System.out.println( slScr );   // ERROR: inherited, same package

	System.out.println( slIPub );   // inherited, same package
	System.out.println( slIPkg );   // inherited, same package
	System.out.println( slIScr );   // inherited, same package

	System.out.println( slRPub );   // inherited, same package
	System.out.println( slRPkg );   // inherited, same package
	System.out.println( slRScr );   // inherited, same package

	System.out.println( slIRPub );   // inherited, same package
	System.out.println( slIRPkg );   // inherited, same package
	System.out.println( slIRScr );   // inherited, same package
    }

    function slHereRead() {
	System.out.println( slHerePub );   // nested, access to script
	System.out.println( slHerePkg );   // nested, access to script
	System.out.println( slHereScr );   // nested, access to script

	System.out.println( slHereIPub );   // nested, access to script
	System.out.println( slHereIPkg );   // nested, access to script
	System.out.println( slHereIScr );   // nested, access to script

	System.out.println( slHereRPub );   // nested, access to script
	System.out.println( slHereRPkg );   // nested, access to script
	System.out.println( slHereRScr );   // nested, access to script

	System.out.println( slHereIRPub );   // nested, access to script
	System.out.println( slHereIRPkg );   // nested, access to script
	System.out.println( slHereIRScr );   // nested, access to script
    }
}

class InhA extends jfxc1250subFailA {
    function slRead() {
	System.out.println( slPub );   // inherited, different package
	System.out.println( slPkg );   // ERROR: inherited, different package
	System.out.println( slScr );   // ERROR: inherited, different package

	System.out.println( slIPub );   // inherited, different package
	System.out.println( slIPkg );   // inherited, different package
	System.out.println( slIScr );   // inherited, different package

	System.out.println( slRPub );   // inherited, different package
	System.out.println( slRPkg );   // inherited, different package
	System.out.println( slRScr );   // inherited, different package

	System.out.println( slIRPub );   // inherited, different package
	System.out.println( slIRPkg );   // inherited, different package
	System.out.println( slIRScr );   // inherited, different package
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

	System.out.println( slHereIPub );   // this script class, direct access
	System.out.println( slHereIPkg );   // this script class, direct access
	System.out.println( slHereIScr );   // this script class, direct access

	System.out.println( slHereRPub );   // this script class, direct access
	System.out.println( slHereRPkg );   // this script class, direct access
	System.out.println( slHereRScr );   // this script class, direct access

	System.out.println( slHereIRPub );   // this script class, direct access
	System.out.println( slHereIRPkg );   // this script class, direct access
	System.out.println( slHereIRScr );   // this script class, direct access
}

function slHereRead() {
	System.out.println( jfxc1250slRead.slHerePub );   // this script class, select access
	System.out.println( jfxc1250slRead.slHerePkg );   // this script class, select access
	System.out.println( jfxc1250slRead.slHereScr );   // this script class, select access

	System.out.println( jfxc1250slRead.slHereIPub );   // this script class, select access
	System.out.println( jfxc1250slRead.slHereIPkg );   // this script class, select access
	System.out.println( jfxc1250slRead.slHereIScr );   // this script class, select access

	System.out.println( jfxc1250slRead.slHereRPub );   // this script class, select access
	System.out.println( jfxc1250slRead.slHereRPkg );   // this script class, select access
	System.out.println( jfxc1250slRead.slHereRScr );   // this script class, select access

	System.out.println( jfxc1250slRead.slHereIRPub );   // this script class, select access
	System.out.println( jfxc1250slRead.slHereIRPkg );   // this script class, select access
	System.out.println( jfxc1250slRead.slHereIRScr );   // this script class, select access
}

function slSubRead() {
	System.out.println( jfxc1250subFail.slPub );   // same package
	System.out.println( jfxc1250subFail.slPkg );   // same package
	System.out.println( jfxc1250subFail.slScr );   // ERROR: same package

	System.out.println( jfxc1250subFail.slIPub );   // same package
	System.out.println( jfxc1250subFail.slIPkg );   // same package
	System.out.println( jfxc1250subFail.slIScr );   // same package

	System.out.println( jfxc1250subFail.slRPub );   // same package
	System.out.println( jfxc1250subFail.slRPkg );   // same package
	System.out.println( jfxc1250subFail.slRScr );   // same package

	System.out.println( jfxc1250subFail.slIRPub );   // same package
	System.out.println( jfxc1250subFail.slIRPkg );   // same package
	System.out.println( jfxc1250subFail.slIRScr );   // same package
}

function slSubARead() {
	System.out.println( jfxc1250subFailA.slPub );   // different package
	System.out.println( jfxc1250subFailA.slPkg );   // ERROR: different package
	System.out.println( jfxc1250subFailA.slScr );   // ERROR: different package

	System.out.println( jfxc1250subFailA.slIPub );   // different package
	System.out.println( jfxc1250subFailA.slIPkg );   // different package
	System.out.println( jfxc1250subFailA.slIScr );   // different package

	System.out.println( jfxc1250subFailA.slRPub );   // different package
	System.out.println( jfxc1250subFailA.slRPkg );   // different package
	System.out.println( jfxc1250subFailA.slRScr );   // different package

	System.out.println( jfxc1250subFailA.slIRPub );   // different package
	System.out.println( jfxc1250subFailA.slIRPkg );   // different package
	System.out.println( jfxc1250subFailA.slIRScr );   // different package
}

function run( ) {
	slInhRead();
	slInhARead();
	slRead();
	slHereRead();
	slSubRead();
	slSubARead();
}
