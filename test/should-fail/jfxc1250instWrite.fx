/**
 * Regression test JFXC-1250 : visibility modifiers
 *
 *     instance variable writes
 *
 * @compilefirst jfxc1250/a/jfxc1250subFailA.fx
 * @compilefirst jfxc1250subFail.fx
 * @test/compile-error
 */
import java.lang.System;
import jfxc1250.a.jfxc1250subFailA;

public class jfxc1250instWrite {
	public var memHerePub = 0;
	protected var memHerePro = 0;
	package var memHerePkg = 0;
	var memHereScr = 0;

	public-init public var memHereIPub = 0;
	public-init protected var memHereIPro = 0;
	public-init package var memHereIPkg = 0;
	public-init var memHereIScr = 0;

	public-read public var memHereRPub = 0;
	public-read protected var memHereRPro = 0;
	public-read package var memHereRPkg = 0;
	public-read var memHereRScr = 0;

	public-init public-read public var memHereIRPub = 0;
	public-init public-read protected var memHereIRPro = 0;
	public-init public-read package var memHereIRPkg = 0;
	public-init public-read var memHereIRScr = 0;
}

class Foo {
	public var fooPub = 0;
	protected var fooPro = 0;
	package var fooPkg = 0;
	var fooScr = 0;

	public-init public var fooIPub = 0;
	public-init protected var fooIPro = 0;
	public-init package var fooIPkg = 0;
	public-init var fooIScr = 0;

	public-read public var fooRPub = 0;
	public-read protected var fooRPro = 0;
	public-read package var fooRPkg = 0;
	public-read var fooRScr = 0;

	public-init public-read public var fooIRPub = 0;
	public-init public-read protected var fooIRPro = 0;
	public-init public-read package var fooIRPkg = 0;
	public-init public-read var fooIRScr = 0;
}

class Inh extends jfxc1250subFail {
    function memWrite() {
	memPub = 99;   // inherited, same package
	memPkg = 99;   // inherited, same package
	memPro = 99;   // inherited, same package
	memScr = 99;   // ERROR: inherited, same package

	memIPub = 99;   // inherited, same package
	memIPkg = 99;   // inherited, same package
	memIPro = 99;   // inherited, same package
	memIScr = 99;   // ERROR: inherited, same package

	memRPub = 99;   // inherited, same package
	memRPro = 99;   // inherited, same package
	memRPkg = 99;   // inherited, same package
	memRScr = 99;   // ERROR: inherited, same package

	memIRPub = 99;   // inherited, same package
	memIRPro = 99;   // inherited, same package
	memIRPkg = 99;   // inherited, same package
	memIRScr = 99;   // ERROR: inherited, same package
    }
}

class InhA extends jfxc1250subFailA {
    function memWrite() {
	memPub = 99;   // inherited, different package
	memPro = 99;   // inherited, different package
	memPkg = 99;   // ERROR: inherited, different package
	memScr = 99;   // ERROR: inherited, different package

	memIPub = 99;   // inherited, different package
	memIPro = 99;   // inherited, different package
	memIPkg = 99;   // ERROR: inherited, different package
	memIScr = 99;   // ERROR: inherited, different package

	memRPub = 99;   // inherited, different package
	memRPro = 99;   // inherited, different package
	memRPkg = 99;   // ERROR: inherited, different package
	memRScr = 99;   // ERROR: inherited, different package

	memIRPub = 99;   // inherited, different package
	memIRPro = 99;   // inherited, different package
	memIRPkg = 99;   // ERROR: inherited, different package
	memIRScr = 99;   // ERROR: inherited, different package
    }
}

function memInhWrite() {
	var vf = Inh {};
	vf.memWrite();
}

function memInhAWrite() {
	var vf = InhA {};
	vf.memWrite();
}

function memHereWrite() {
	var vf = jfxc1250instWrite {};
	vf.memHerePub = 99;   // script class, select access
	vf.memHerePkg = 99;   // script class, select access
	vf.memHerePro = 99;   // script class, select access
	vf.memHereScr = 99;   // script class, select access

	vf.memHereIPub = 99;   // script class, select access
	vf.memHereIPkg = 99;   // script class, select access
	vf.memHereIPro = 99;   // script class, select access
	vf.memHereIScr = 99;   // script class, select access

	vf.memHereRPub = 99;   // script class, select access
	vf.memHereRPro = 99;   // script class, select access
	vf.memHereRPkg = 99;   // script class, select access
	vf.memHereRScr = 99;   // script class, select access

	vf.memHereIRPub = 99;   // script class, select access
	vf.memHereIRPro = 99;   // script class, select access
	vf.memHereIRPkg = 99;   // script class, select access
	vf.memHereIRScr = 99;   // script class, select access
}

function memFooWrite() {
	var vf = Foo {};
	vf.fooPub = 99;   // nested class
	vf.fooPkg = 99;   // nested class
	vf.fooPro = 99;   // nested class
	vf.fooScr = 99;   // nested class

	vf.fooIPub = 99;   // nested class
	vf.fooIPkg = 99;   // nested class
	vf.fooIPro = 99;   // nested class
	vf.fooIScr = 99;   // nested class

	vf.fooRPub = 99;   // nested class
	vf.fooRPro = 99;   // nested class
	vf.fooRPkg = 99;   // nested class
	vf.fooRScr = 99;   // nested class

	vf.fooIRPub = 99;   // nested class
	vf.fooIRPro = 99;   // nested class
	vf.fooIRPkg = 99;   // nested class
	vf.fooIRScr = 99;   // nested class
}

function memSubWrite() {
	var vf = jfxc1250subFail {};
	vf.memPub = 99;   // same package
	vf.memPkg = 99;   // same package
	vf.memPro = 99;   // same package
	vf.memScr = 99;   // ERROR: same package

	vf.memIPub = 99;   // same package
	vf.memIPkg = 99;   // same package
	vf.memIPro = 99;   // same package
	vf.memIScr = 99;   // ERROR: same package

	vf.memRPub = 99;   // same package
	vf.memRPro = 99;   // same package
	vf.memRPkg = 99;   // same package
	vf.memRScr = 99;   // ERROR: same package

	vf.memIRPub = 99;   // same package
	vf.memIRPro = 99;   // same package
	vf.memIRPkg = 99;   // same package
	vf.memIRScr = 99;   // ERROR: same package
}

function memSubAWrite() {
	var vf = jfxc1250subFailA {};
	vf.memPub = 99;   // different package
	vf.memPkg = 99;   // ERROR: different package
	vf.memPro = 99;   // ERROR: different package
	vf.memScr = 99;   // ERROR: different package

	vf.memIPub = 99;   // different package
	vf.memIPkg = 99;   // ERROR: different package
	vf.memIPro = 99;   // ERROR: different package
	vf.memIScr = 99;   // ERROR: different package

	vf.memRPub = 99;   // different package
	vf.memRPro = 99;   // ERROR: different package
	vf.memRPkg = 99;   // ERROR: different package
	vf.memRScr = 99;   // ERROR: different package

	vf.memIRPub = 99;   // different package
	vf.memIRPro = 99;   // ERROR: different package
	vf.memIRPkg = 99;   // ERROR: different package
	vf.memIRScr = 99;   // ERROR: different package
}

function run() {
	memInhWrite();
	memInhAWrite();
	memHereWrite();
	memFooWrite();
	memSubWrite();
	memSubAWrite();
}
