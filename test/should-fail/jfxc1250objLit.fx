/**
 * Regression test JFXC-1250 : visibility modifiers
 *
 *     instance variable initialization
 *
 * @compilefirst jfxc1250/a/jfxc1250subFailA.fx
 * @compilefirst jfxc1250subFail.fx
 * @test/compile-error
 */
import java.lang.System;
import jfxc1250.a.jfxc1250subFailA;

public class jfxc1250objLit {
	public var memHerePub = 0;
	protected var memHerePro = 0;
	package var memHerePkg = 0;
	var memHereScr = 0;

	public-init protected var memHereIPro = 0;
	public-init package var memHereIPkg = 0;
	public-init var memHereIScr = 0;

	public-read protected var memHereRPro = 0;
	public-read package var memHereRPkg = 0;
	public-read var memHereRScr = 0;
}

class Foo {
	public var fooPub = 0;
	protected var fooPro = 0;
	package var fooPkg = 0;
	var fooScr = 0;

	public-init protected var fooIPro = 0;
	public-init package var fooIPkg = 0;
	public-init var fooIScr = 0;

	public-read protected var fooRPro = 0;
	public-read package var fooRPkg = 0;
	public-read var fooRScr = 0;
}

class Inh extends jfxc1250subFail {
    function memInit() {
	Inh {
	    memPub: 123,   // inherited, same package
	    memPkg: 123,   // inherited, same package
	    memPro: 123,   // inherited, same package
	    memScr: 123,   // ERROR: inherited, same package

	    memIPkg: 123,   // inherited, same package
	    memIPro: 123,   // inherited, same package
	    memIScr: 123,   // inherited, same package

	    memRPro: 123,   // inherited, same package
	    memRPkg: 123,   // inherited, same package
	    memRScr: 123,   // ERROR: inherited, same package
	}
    }
}

class InhA extends jfxc1250subFailA {
    function memInit() {
	InhA {
	    memPub: 123,   // inherited, different package
	    memPkg: 123,   // ERROR: inherited, different package
	    memPro: 123,   // inherited, different package
	    memScr: 123,   // ERROR: inherited, different package

	    memIPkg: 123,   // inherited, different package
	    memIPro: 123,   // inherited, different package
	    memIScr: 123,   // inherited, different package

	    memRPro: 123,   // inherited, different package
	    memRPkg: 123,   // ERROR: inherited, different package
	    memRScr: 123,   // ERROR: inherited, different package
	}
    }
}

function memInhInit() {
	var vf = Inh {};
	vf.memInit();
}

function memInhAInit() {
	var vf = InhA {};
	vf.memInit();
}

function memHereInit() {
	jfxc1250objLit {
	    memHerePub: 123,   // script class, select access
	    memHerePkg: 123,   // script class, select access
	    memHerePro: 123,   // script class, select access
	    memHereScr: 123,   // script class, select access

	    memHereIPkg: 123,   // script class, select access
	    memHereIPro: 123,   // script class, select access
	    memHereIScr: 123,   // script class, select access

	    memHereRPro: 123,   // script class, select access
	    memHereRPkg: 123,   // script class, select access
	    memHereRScr: 123,   // script class, select access
	}
}

function memFooInit() {
	Foo {
	    fooPub: 123,   // nested class
	    fooPkg: 123,   // nested class
	    fooPro: 123,   // nested class
	    fooScr: 123,   // nested class

	    fooIPkg: 123,   // nested class
	    fooIPro: 123,   // nested class
	    fooIScr: 123,   // nested class

	    fooRPro: 123,   // nested class
	    fooRPkg: 123,   // nested class
	    fooRScr: 123,   // nested class
	}
}

function memSubInit() {
	jfxc1250subFail {
	    memPub: 123,   // same package
	    memPkg: 123,   // same package
	    memPro: 123,   // same package
	    memScr: 123,   // ERROR: same package

	    memIPkg: 123,   // same package
	    memIPro: 123,   // same package
	    memIScr: 123,   // same package

	    memRPro: 123,   // same package
	    memRPkg: 123,   // same package
	    memRScr: 123,   // ERROR: same package
	}
}

function memSubAInit() {
	jfxc1250subFailA {
	    memPub: 123,   // different package
	    memPkg: 123,   // ERROR: different package
	    memPro: 123,   // ERROR: different package
	    memScr: 123,   // ERROR: different package

	    memIPkg: 123,   // different package
	    memIPro: 123,   // different package
	    memIScr: 123,   // different package

	    memRPro: 123,   // ERROR: different package
	    memRPkg: 123,   // ERROR: different package
	    memRScr: 123,   // ERROR: different package
	}
}

function run() {
	memInhInit();
	memInhAInit();
	memHereInit();
	memFooInit();
	memSubInit();
	memSubAInit();
}
