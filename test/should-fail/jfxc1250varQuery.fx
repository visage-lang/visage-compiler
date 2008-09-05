/**
 * Regression test JFXC-1250 : visibility modifiers
 *
 *     isInitialized() access
 *
 * @compilefirst jfxc1250/a/jfxc1250subFailA.fx
 * @compilefirst jfxc1250subFail.fx
 * @test/compile-error
 */

import jfxc1250.a.jfxc1250subFailA;

public class jfxc1250varQuery {
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
    function memVarQuery() {
	isInitialized(memPub);   // inherited, same package
	isInitialized(memPro);   // inherited, same package
	isInitialized(memPkg);   // inherited, same package
	isInitialized(memScr);   // ERROR: inherited, same package

	isInitialized(memIPub);   // inherited, same package
	isInitialized(memIPro);   // inherited, same package
	isInitialized(memIPkg);   // inherited, same package
	isInitialized(memIScr);   // ERROR: inherited, same package

	isInitialized(memRPub);   // inherited, same package
	isInitialized(memRPro);   // inherited, same package
	isInitialized(memRPkg);   // inherited, same package
	isInitialized(memRScr);   // ERROR: inherited, same package

	isInitialized(memIRPub);   // inherited, same package
	isInitialized(memIRPro);   // inherited, same package
	isInitialized(memIRPkg);   // inherited, same package
	isInitialized(memIRScr);   // ERROR: inherited, same package
    }
}

class InhA extends jfxc1250subFailA {
    function memVarQuery() {
	isInitialized(memPub);   // inherited, different package
	isInitialized(memPro);   // inherited, different package
	isInitialized(memPkg);   // ERROR: inherited, different package
	isInitialized(memScr);   // ERROR: inherited, different package

	isInitialized(memIPub);   // inherited, different package
	isInitialized(memIPro);   // inherited, different package
	isInitialized(memIPkg);   // ERROR: inherited, different package
	isInitialized(memIScr);   // ERROR: inherited, different package

	isInitialized(memRPub);   // inherited, different package
	isInitialized(memRPro);   // inherited, different package
	isInitialized(memRPkg);   // ERROR: inherited, different package
	isInitialized(memRScr);   // ERROR: inherited, different package

	isInitialized(memIRPub);   // inherited, different package
	isInitialized(memIRPro);   // inherited, different package
	isInitialized(memIRPkg);   // ERROR: inherited, different package
	isInitialized(memIRScr);   // ERROR: inherited, different package
    }
}

function memInhVarQuery() {
	var vf = Inh {};
	vf.memVarQuery();
}

function memInhAVarQuery() {
	var vf = InhA {};
	vf.memVarQuery();
}

function memHereVarQuery() {
	var vf = jfxc1250varQuery {};
	isInitialized(vf.memHerePub);   // script class, select access
	isInitialized(vf.memHerePro);   // script class, select access
	isInitialized(vf.memHerePkg);   // script class, select access
	isInitialized(vf.memHereScr);   // script class, select access

	isInitialized(vf.memHereIPub);   // script class, select access
	isInitialized(vf.memHereIPro);   // script class, select access
	isInitialized(vf.memHereIPkg);   // script class, select access
	isInitialized(vf.memHereIScr);   // script class, select access

	isInitialized(vf.memHereRPub);   // script class, select access
	isInitialized(vf.memHereRPro);   // script class, select access
	isInitialized(vf.memHereRPkg);   // script class, select access
	isInitialized(vf.memHereRScr);   // script class, select access

	isInitialized(vf.memHereIRPub);   // script class, select access
	isInitialized(vf.memHereIRPro);   // script class, select access
	isInitialized(vf.memHereIRPkg);   // script class, select access
	isInitialized(vf.memHereIRScr);   // script class, select access
}

function memFooVarQuery() {
	var vf = Foo {};
	isInitialized(vf.fooPub);   // nested class
	isInitialized(vf.fooPro);   // nested class
	isInitialized(vf.fooPkg);   // nested class
	isInitialized(vf.fooScr);   // nested class

	isInitialized(vf.fooIPub);   // nested class
	isInitialized(vf.fooIPro);   // nested class
	isInitialized(vf.fooIPkg);   // nested class
	isInitialized(vf.fooIScr);   // nested class

	isInitialized(vf.fooRPub);   // nested class
	isInitialized(vf.fooRPro);   // nested class
	isInitialized(vf.fooRPkg);   // nested class
	isInitialized(vf.fooRScr);   // nested class

	isInitialized(vf.fooIRPub);   // nested class
	isInitialized(vf.fooIRPro);   // nested class
	isInitialized(vf.fooIRPkg);   // nested class
	isInitialized(vf.fooIRScr);   // nested class
}

function memSubVarQuery() {
	var vf = jfxc1250subFail {};
	isInitialized(vf.memPub);   // same package
	isInitialized(vf.memPro);   // same package
	isInitialized(vf.memPkg);   // same package
	isInitialized(vf.memScr);   // ERROR: same package

	isInitialized(vf.memIPub);   // same package
	isInitialized(vf.memIPro);   // same package
	isInitialized(vf.memIPkg);   // same package
	isInitialized(vf.memIScr);   // ERROR: same package

	isInitialized(vf.memRPub);   // same package
	isInitialized(vf.memRPro);   // same package
	isInitialized(vf.memRPkg);   // same package
	isInitialized(vf.memRScr);   // ERROR: same package

	isInitialized(vf.memIRPub);   // same package
	isInitialized(vf.memIRPro);   // same package
	isInitialized(vf.memIRPkg);   // same package
	isInitialized(vf.memIRScr);   // ERROR: same package
}

function memSubAVarQuery() {
	var vf = jfxc1250subFailA {};
	isInitialized(vf.memPub);   // different package
	isInitialized(vf.memPro);   // ERROR: different package
	isInitialized(vf.memPkg);   // ERROR: different package
	isInitialized(vf.memScr);   // ERROR: different package

	isInitialized(vf.memIPub);   // different package
	isInitialized(vf.memIPro);   // ERROR: different package
	isInitialized(vf.memIPkg);   // ERROR: different package
	isInitialized(vf.memIScr);   // ERROR: different package

	isInitialized(vf.memRPub);   // different package
	isInitialized(vf.memRPro);   // ERROR: different package
	isInitialized(vf.memRPkg);   // ERROR: different package
	isInitialized(vf.memRScr);   // ERROR: different package

	isInitialized(vf.memIRPub);   // different package
	isInitialized(vf.memIRPro);   // ERROR: different package
	isInitialized(vf.memIRPkg);   // ERROR: different package
	isInitialized(vf.memIRScr);   // ERROR: different package
}

function run() {
	memInhVarQuery();
	memInhAVarQuery();
	memHereVarQuery();
	memFooVarQuery();
	memSubVarQuery();
	memSubAVarQuery();
}
