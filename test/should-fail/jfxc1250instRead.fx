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

public class jfxc1250instRead {
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
    function memRead() {
	System.out.println( memPub );   // inherited, same package
	System.out.println( memPkg );   // inherited, same package
	System.out.println( memPro );   // inherited, same package
	System.out.println( memScr );   // ERROR: inherited, same package

	System.out.println( memIPub );   // inherited, same package
	System.out.println( memIPkg );   // inherited, same package
	System.out.println( memIPro );   // inherited, same package
	System.out.println( memIScr );   // inherited, same package

	System.out.println( memRPub );   // inherited, same package
	System.out.println( memRPro );   // inherited, same package
	System.out.println( memRPkg );   // inherited, same package
	System.out.println( memRScr );   // inherited, same package

	System.out.println( memIRPub );   // inherited, same package
	System.out.println( memIRPro );   // inherited, same package
	System.out.println( memIRPkg );   // inherited, same package
	System.out.println( memIRScr );   // inherited, same package
    }
}

class InhA extends jfxc1250subFailA {
    function memRead() {
	System.out.println( memPub );   // inherited, different package
	System.out.println( memPkg );   // ERROR: inherited, different package
	System.out.println( memPro );   // inherited, different package
	System.out.println( memScr );   // ERROR: inherited, different package

	System.out.println( memIPub );   // inherited, different package
	System.out.println( memIPkg );   // inherited, different package
	System.out.println( memIPro );   // inherited, different package
	System.out.println( memIScr );   // inherited, different package

	System.out.println( memRPub );   // inherited, different package
	System.out.println( memRPro );   // inherited, different package
	System.out.println( memRPkg );   // inherited, different package
	System.out.println( memRScr );   // inherited, different package

	System.out.println( memIRPub );   // inherited, different package
	System.out.println( memIRPro );   // inherited, different package
	System.out.println( memIRPkg );   // inherited, different package
	System.out.println( memIRScr );   // inherited, different package
    }
}

function memInhRead() {
	var vf = Inh {};
	vf.memRead();
}

function memInhARead() {
	var vf = InhA {};
	vf.memRead();
}

function memHereRead() {
	var vf = jfxc1250instRead {};
	System.out.println( vf.memHerePub );   // script class, select access
	System.out.println( vf.memHerePkg );   // script class, select access
	System.out.println( vf.memHerePro );   // script class, select access
	System.out.println( vf.memHereScr );   // script class, select access

	System.out.println( vf.memHereIPub );   // script class, select access
	System.out.println( vf.memHereIPkg );   // script class, select access
	System.out.println( vf.memHereIPro );   // script class, select access
	System.out.println( vf.memHereIScr );   // script class, select access

	System.out.println( vf.memHereRPub );   // script class, select access
	System.out.println( vf.memHereRPro );   // script class, select access
	System.out.println( vf.memHereRPkg );   // script class, select access
	System.out.println( vf.memHereRScr );   // script class, select access

	System.out.println( vf.memHereIRPub );   // script class, select access
	System.out.println( vf.memHereIRPro );   // script class, select access
	System.out.println( vf.memHereIRPkg );   // script class, select access
	System.out.println( vf.memHereIRScr );   // script class, select access
}

function memFooRead() {
	var vf = Foo {};
	System.out.println( vf.fooPub );   // nested class
	System.out.println( vf.fooPkg );   // nested class
	System.out.println( vf.fooPro );   // nested class
	System.out.println( vf.fooScr );   // nested class

	System.out.println( vf.fooIPub );   // nested class
	System.out.println( vf.fooIPkg );   // nested class
	System.out.println( vf.fooIPro );   // nested class
	System.out.println( vf.fooIScr );   // nested class

	System.out.println( vf.fooRPub );   // nested class
	System.out.println( vf.fooRPro );   // nested class
	System.out.println( vf.fooRPkg );   // nested class
	System.out.println( vf.fooRScr );   // nested class

	System.out.println( vf.fooIRPub );   // nested class
	System.out.println( vf.fooIRPro );   // nested class
	System.out.println( vf.fooIRPkg );   // nested class
	System.out.println( vf.fooIRScr );   // nested class
}

function memSubRead() {
	var vf = jfxc1250subFail {};
	System.out.println( vf.memPub );   // same package
	System.out.println( vf.memPkg );   // same package
	System.out.println( vf.memPro );   // same package
	System.out.println( vf.memScr );   // ERROR: same package

	System.out.println( vf.memIPub );   // same package
	System.out.println( vf.memIPkg );   // same package
	System.out.println( vf.memIPro );   // same package
	System.out.println( vf.memIScr );   // same package

	System.out.println( vf.memRPub );   // same package
	System.out.println( vf.memRPro );   // same package
	System.out.println( vf.memRPkg );   // same package
	System.out.println( vf.memRScr );   // same package

	System.out.println( vf.memIRPub );   // same package
	System.out.println( vf.memIRPro );   // same package
	System.out.println( vf.memIRPkg );   // same package
	System.out.println( vf.memIRScr );   // same package
}

function memSubARead() {
	var vf = jfxc1250subFailA {};
	System.out.println( vf.memPub );   // different package
	System.out.println( vf.memPkg );   // ERROR: different package
	System.out.println( vf.memPro );   // ERROR: different package
	System.out.println( vf.memScr );   // ERROR: different package

	System.out.println( vf.memIPub );   // different package
	System.out.println( vf.memIPkg );   // different package
	System.out.println( vf.memIPro );   // different package
	System.out.println( vf.memIScr );   // different package

	System.out.println( vf.memRPub );   // different package
	System.out.println( vf.memRPro );   // different package
	System.out.println( vf.memRPkg );   // different package
	System.out.println( vf.memRScr );   // different package

	System.out.println( vf.memIRPub );   // different package
	System.out.println( vf.memIRPro );   // different package
	System.out.println( vf.memIRPkg );   // different package
	System.out.println( vf.memIRScr );   // different package
}

function run( ) {
	memInhRead();
	memInhARead();
	memHereRead();
	memFooRead();
	memSubRead();
	memSubARead();
}
