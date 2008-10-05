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

public-read package var slHereRPkg = 0;
public-read var slHereRScr = 0;

class Inh extends jfxc1250subFail {

    function slWrite() {
	slPub = 99;   // inherited, same package
	slPkg = 99;   // inherited, same package
	slScr = 99;   // ERROR: inherited, same package

	slRPkg = 99;   // inherited, same package
	slRScr = 99;   // ERROR: inherited, same package
    }

    function slHereWrite() {
	slHerePub = 99;   // nested, access to script
	slHerePkg = 99;   // nested, access to script
	slHereScr = 99;   // nested, access to script

	slHereRPkg = 99;   // nested, access to script
	slHereRScr = 99;   // nested, access to script
    }
}

class InhA extends jfxc1250subFailA {

    function slWrite() {
	slPub = 99;   // inherited, different package
	slPkg = 99;   // ERROR: inherited, different package
	slScr = 99;   // ERROR: inherited, different package

	slRPkg = 99;   // ERROR: inherited, different package
	slRScr = 99;   // ERROR: inherited, different package
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

	slHereRPkg = 99;   // this script class, direct access
	slHereRScr = 99;   // this script class, direct access
}

function slHereWrite() {
	jfxc1250slWrite.slHerePub = 99;   // this script class, select access
	jfxc1250slWrite.slHerePkg = 99;   // this script class, select access
	jfxc1250slWrite.slHereScr = 99;   // this script class, select access

	jfxc1250slWrite.slHereRPkg = 99;   // this script class, select access
	jfxc1250slWrite.slHereRScr = 99;   // this script class, select access
}

function slSubWrite() {
	jfxc1250subFail.slPub = 99;   // same package
	jfxc1250subFail.slPkg = 99;   // same package
	jfxc1250subFail.slScr = 99;   // ERROR: same package

	jfxc1250subFail.slRPkg = 99;   // same package
	jfxc1250subFail.slRScr = 99;   // ERROR: same package
}

function slSubAWrite() {
	jfxc1250subFailA.slPub = 99;   // different package
	jfxc1250subFailA.slPkg = 99;   // ERROR: different package
	jfxc1250subFailA.slScr = 99;   // ERROR: different package

	jfxc1250subFailA.slRPkg = 99;   // ERROR: different package
	jfxc1250subFailA.slRScr = 99;   // ERROR: different package
}

function run( ) {
	slInhWrite();
	slInhAWrite();
	slWrite();
	slHereWrite();
	slSubWrite();
	slSubAWrite();
}
