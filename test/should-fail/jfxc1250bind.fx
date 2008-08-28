/**
 * Regression test JFXC-1250 : visibility modifiers
 *
 *     instance variable bind
 *
 * @compilefirst jfxc1250/a/jfxc1250subFailA.fx
 * @compilefirst jfxc1250subFail.fx
 * @test/compile-error
 */
import java.lang.System;
import jfxc1250.a.jfxc1250subFailA;

var toThis = 0;

public class jfxc1250bind {
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
    function memInit() {
	Inh {
	    memPub: bind toThis,   // inherited, same package
	    memPkg: bind toThis,   // inherited, same package
	    memPro: bind toThis,   // inherited, same package
	    memScr: bind toThis,   // ERROR: inherited, same package

	    memIPub: bind toThis,   // inherited, same package
	    memIPkg: bind toThis,   // inherited, same package
	    memIPro: bind toThis,   // inherited, same package
	    memIScr: bind toThis,   // ERROR:  inherited, same package

	    memRPub: bind toThis,   // inherited, same package
	    memRPro: bind toThis,   // inherited, same package
	    memRPkg: bind toThis,   // inherited, same package
	    memRScr: bind toThis,   // ERROR: inherited, same package

	    memIRPub: bind toThis,   // inherited, same package
	    memIRPro: bind toThis,   // inherited, same package
	    memIRPkg: bind toThis,   // inherited, same package
	    memIRScr: bind toThis,   // ERROR:  inherited, same package
	}
    }
}

class InhA extends jfxc1250subFailA {
    function memInit() {
	InhA {
	    memPub: bind toThis,   // inherited, different package
	    memPro: bind toThis,   // inherited, different package
	    memPkg: bind toThis,   // ERROR: inherited, different package
	    memScr: bind toThis,   // ERROR: inherited, different package

	    memIPub: bind toThis,   // inherited, different package
	    memIPro: bind toThis,   // inherited, different package
	    memIPkg: bind toThis,   // ERROR:  inherited, different package
	    memIScr: bind toThis,   // ERROR:  inherited, different package

	    memRPub: bind toThis,   // inherited, different package
	    memRPro: bind toThis,   // inherited, different package
	    memRPkg: bind toThis,   // ERROR: inherited, different package
	    memRScr: bind toThis,   // ERROR: inherited, different package

	    memIRPub: bind toThis,   // inherited, different package
	    memIRPro: bind toThis,   // inherited, different package
	    memIRPkg: bind toThis,   // ERROR:  inherited, different package
	    memIRScr: bind toThis,   // ERROR:  inherited, different package
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
	jfxc1250bind {
	    memHerePub: bind toThis,   // script class, select access
	    memHerePro: bind toThis,   // script class, select access
	    memHerePkg: bind toThis,   // script class, select access
	    memHereScr: bind toThis,   // script class, select access

	    memHereIPub: bind toThis,   // script class, select access
	    memHereIPro: bind toThis,   // script class, select access
	    memHereIPkg: bind toThis,   // script class, select access
	    memHereIScr: bind toThis,   // script class, select access

	    memHereRPub: bind toThis,   // script class, select access
	    memHereRPro: bind toThis,   // script class, select access
	    memHereRPkg: bind toThis,   // script class, select access
	    memHereRScr: bind toThis,   // script class, select access

	    memHereIRPub: bind toThis,   // script class, select access
	    memHereIRPro: bind toThis,   // script class, select access
	    memHereIRPkg: bind toThis,   // script class, select access
	    memHereIRScr: bind toThis,   // script class, select access
	}
}

function memFooInit() {
	Foo {
	    fooPub: bind toThis,   // nested class
	    fooPro: bind toThis,   // nested class
	    fooPkg: bind toThis,   // nested class
	    fooScr: bind toThis,   // nested class

	    fooIPub: bind toThis,   // nested class
	    fooIPro: bind toThis,   // nested class
	    fooIPkg: bind toThis,   // nested class
	    fooIScr: bind toThis,   // nested class

	    fooRPub: bind toThis,   // nested class
	    fooRPro: bind toThis,   // nested class
	    fooRPkg: bind toThis,   // nested class
	    fooRScr: bind toThis,   // nested class

	    fooIRPub: bind toThis,   // nested class
	    fooIRPro: bind toThis,   // nested class
	    fooIRPkg: bind toThis,   // nested class
	    fooIRScr: bind toThis,   // nested class
	}
}

function memSubInit() {
	jfxc1250subFail {
	    memPub: bind toThis,   // same package
	    memPro: bind toThis,   // same package
	    memPkg: bind toThis,   // same package
	    memScr: bind toThis,   // ERROR: same package

	    memIPub: bind toThis,   // same package
	    memIPro: bind toThis,   // same package
	    memIPkg: bind toThis,   // same package
	    memIScr: bind toThis,   // ERROR:  same package

	    memRPub: bind toThis,   // same package
	    memRPro: bind toThis,   // same package
	    memRPkg: bind toThis,   // same package
	    memRScr: bind toThis,   // ERROR: same package

	    memIRPub: bind toThis,   // same package
	    memIRPro: bind toThis,   // same package
	    memIRPkg: bind toThis,   // same package
	    memIRScr: bind toThis,   // ERROR:  same package
	}
}

function memSubAInit() {
	jfxc1250subFailA {
	    memPub: bind toThis,   // different package
	    memPro: bind toThis,   // ERROR: different package
	    memPkg: bind toThis,   // ERROR: different package
	    memScr: bind toThis,   // ERROR: different package

	    memIPub: bind toThis,   // different package
	    memIPro: bind toThis,   // ERROR:  different package
	    memIPkg: bind toThis,   // ERROR:  different package
	    memIScr: bind toThis,   // ERROR:  different package

	    memRPub: bind toThis,   // different package
	    memRPro: bind toThis,   // ERROR: different package
	    memRPkg: bind toThis,   // ERROR: different package
	    memRScr: bind toThis,   // ERROR: different package

	    memIRPub: bind toThis,   // different package
	    memIRPro: bind toThis,   // ERROR:  different package
	    memIRPkg: bind toThis,   // ERROR:  different package
	    memIRScr: bind toThis,   // ERROR:  different package
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
