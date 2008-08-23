/**
 * Regression test JFXC-1250 : visibility modifiers
 *
 *     Script-level variable writes
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

    function slWrite() {
	slPub = 99;   // inherited, same package
	slPkg = 99;   // inherited, same package
	slScr = 99;   // ERROR: inherited, same package

	slIPub = 99;   // inherited, same package
	slIPkg = 99;   // inherited, same package
	slIScr = 99;   // ERROR: inherited, same package

	slRPub = 99;   // inherited, same package
	slRPkg = 99;   // inherited, same package
	slRScr = 99;   // ERROR: inherited, same package

	slIRPub = 99;   // inherited, same package
	slIRPkg = 99;   // inherited, same package
	slIRScr = 99;   // ERROR: inherited, same package
    }

    function slHereWrite() {
	slHerePub = 99;   // nested, access to script
	slHerePkg = 99;   // nested, access to script
	slHereScr = 99;   // nested, access to script

	slHereIPub = 99;   // nested, access to script
	slHereIPkg = 99;   // nested, access to script
	slHereIScr = 99;   // nested, access to script

	slHereRPub = 99;   // nested, access to script
	slHereRPkg = 99;   // nested, access to script
	slHereRScr = 99;   // nested, access to script

	slHereIRPub = 99;   // nested, access to script
	slHereIRPkg = 99;   // nested, access to script
	slHereIRScr = 99;   // nested, access to script
    }
}

class InhA extends jfxc1250subFailA {

    function slWrite() {
	slPub = 99;   // inherited, different package
	slPkg = 99;   // ERROR: inherited, different package
	slScr = 99;   // ERROR: inherited, different package

	slIPub = 99;   // inherited, different package
	slIPkg = 99;   // ERROR: inherited, different package
	slIScr = 99;   // ERROR: inherited, different package

	slRPub = 99;   // inherited, different package
	slRPkg = 99;   // ERROR: inherited, different package
	slRScr = 99;   // ERROR: inherited, different package

	slIRPub = 99;   // inherited, different package
	slIRPkg = 99;   // ERROR: inherited, different package
	slIRScr = 99;   // ERROR: inherited, different package
   }
}

function slInhWrite() {
	var vf = Inh {};
	vf.slWrite();
	vf.slHereWrite();
}

function slInhAWrite() {
	var vf = InhA {};
	vf.slWrite();
}

function slWrite() {
	slHerePub = 99;   // this script class, direct access
	slHerePkg = 99;   // this script class, direct access
	slHereScr = 99;   // this script class, direct access

	slHereIPub = 99;   // this script class, direct access
	slHereIPkg = 99;   // this script class, direct access
	slHereIScr = 99;   // this script class, direct access

	slHereRPub = 99;   // this script class, direct access
	slHereRPkg = 99;   // this script class, direct access
	slHereRScr = 99;   // this script class, direct access

	slHereIRPub = 99;   // this script class, direct access
	slHereIRPkg = 99;   // this script class, direct access
	slHereIRScr = 99;   // this script class, direct access
}

function slHereWrite() {
	jfxc1250slWrite.slHerePub = 99;   // this script class, select access
	jfxc1250slWrite.slHerePkg = 99;   // this script class, select access
	jfxc1250slWrite.slHereScr = 99;   // this script class, select access

	jfxc1250slWrite.slHereIPub = 99;   // this script class, select access
	jfxc1250slWrite.slHereIPkg = 99;   // this script class, select access
	jfxc1250slWrite.slHereIScr = 99;   // this script class, select access

	jfxc1250slWrite.slHereRPub = 99;   // this script class, select access
	jfxc1250slWrite.slHereRPkg = 99;   // this script class, select access
	jfxc1250slWrite.slHereRScr = 99;   // this script class, select access

	jfxc1250slWrite.slHereIRPub = 99;   // this script class, select access
	jfxc1250slWrite.slHereIRPkg = 99;   // this script class, select access
	jfxc1250slWrite.slHereIRScr = 99;   // this script class, select access
}

function slSubWrite() {
	jfxc1250subFail.slPub = 99;   // same package
	jfxc1250subFail.slPkg = 99;   // same package
	jfxc1250subFail.slScr = 99;   // ERROR: same package

	jfxc1250subFail.slIPub = 99;   // same package
	jfxc1250subFail.slIPkg = 99;   // same package
	jfxc1250subFail.slIScr = 99;   // ERROR: same package

	jfxc1250subFail.slRPub = 99;   // same package
	jfxc1250subFail.slRPkg = 99;   // same package
	jfxc1250subFail.slRScr = 99;   // ERROR: same package

	jfxc1250subFail.slIRPub = 99;   // same package
	jfxc1250subFail.slIRPkg = 99;   // same package
	jfxc1250subFail.slIRScr = 99;   // ERROR: same package
}

function slSubAWrite() {
	jfxc1250subFailA.slPub = 99;   // different package
	jfxc1250subFailA.slPkg = 99;   // ERROR: different package
	jfxc1250subFailA.slScr = 99;   // ERROR: different package

	jfxc1250subFailA.slIPub = 99;   // different package
	jfxc1250subFailA.slIPkg = 99;   // ERROR: different package
	jfxc1250subFailA.slIScr = 99;   // ERROR: different package

	jfxc1250subFailA.slRPub = 99;   // different package
	jfxc1250subFailA.slRPkg = 99;   // ERROR: different package
	jfxc1250subFailA.slRScr = 99;   // ERROR: different package

	jfxc1250subFailA.slIRPub = 99;   // different package
	jfxc1250subFailA.slIRPkg = 99;   // ERROR: different package
	jfxc1250subFailA.slIRScr = 99;   // ERROR: different package
}

function run( ) {
	slInhWrite();
	slInhAWrite();
	slWrite();
	slHereWrite();
	slSubWrite();
	slSubAWrite();
}
