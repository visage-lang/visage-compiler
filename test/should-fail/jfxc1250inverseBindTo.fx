/**
 * Regression test JFXC-1250 : visibility modifiers
 *
 *     bind with inverse to instance variable
 *
 * @compilefirst jfxc1250/a/jfxc1250subFailA.fx
 * @compilefirst jfxc1250subFail.fx
 * @test/compile-error
 */
import java.lang.System;
import jfxc1250.a.jfxc1250subFailA;

public class jfxc1250inverseBindTo {
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
	var toThis = 0;

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
    function memInvBind() {
	//Foo { toThis: bind ... with inverse };
	Foo { toThis: bind memPub with inverse };   // inherited, same package
	Foo { toThis: bind memPro with inverse };   // inherited, same package
	Foo { toThis: bind memPkg with inverse };   // inherited, same package
	Foo { toThis: bind memScr with inverse };   // ERROR: inherited, same package

	Foo { toThis: bind memIPub with inverse };   // inherited, same package
	Foo { toThis: bind memIPro with inverse };   // inherited, same package
	Foo { toThis: bind memIPkg with inverse };   // inherited, same package
	Foo { toThis: bind memIScr with inverse };   // ERROR: inherited, same package

	Foo { toThis: bind memRPub with inverse };   // inherited, same package
	Foo { toThis: bind memRPro with inverse };   // inherited, same package
	Foo { toThis: bind memRPkg with inverse };   // inherited, same package
	Foo { toThis: bind memRScr with inverse };   // ERROR: inherited, same package

	Foo { toThis: bind memIRPub with inverse };   // inherited, same package
	Foo { toThis: bind memIRPro with inverse };   // inherited, same package
	Foo { toThis: bind memIRPkg with inverse };   // inherited, same package
	Foo { toThis: bind memIRScr with inverse };   // ERROR: inherited, same package
    }
}

class InhA extends jfxc1250subFailA {
    function memInvBind() {
	Foo { toThis: bind memPub with inverse };   // inherited, different package
	Foo { toThis: bind memPro with inverse };   // inherited, different package
	Foo { toThis: bind memPkg with inverse };   // ERROR: inherited, different package
	Foo { toThis: bind memScr with inverse };   // ERROR: inherited, different package

	Foo { toThis: bind memIPub with inverse };   // inherited, different package
	Foo { toThis: bind memIPro with inverse };   // inherited, different package
	Foo { toThis: bind memIPkg with inverse };   // ERROR: inherited, different package
	Foo { toThis: bind memIScr with inverse };   // ERROR: inherited, different package

	Foo { toThis: bind memRPub with inverse };   // inherited, different package
	Foo { toThis: bind memRPro with inverse };   // inherited, different package
	Foo { toThis: bind memRPkg with inverse };   // ERROR: inherited, different package
	Foo { toThis: bind memRScr with inverse };   // ERROR: inherited, different package

	Foo { toThis: bind memIRPub with inverse };   // inherited, different package
	Foo { toThis: bind memIRPro with inverse };   // inherited, different package
	Foo { toThis: bind memIRPkg with inverse };   // ERROR: inherited, different package
	Foo { toThis: bind memIRScr with inverse };   // ERROR: inherited, different package
    }
}

function memInhInvBind() {
	var vf = Inh {};
	vf.memInvBind();
}

function memInhAInvBind() {
	var vf = InhA {};
	vf.memInvBind();
}

function memHereInvBind() {
	var vf = jfxc1250inverseBindTo {};
	Foo { toThis: bind vf.memHerePub with inverse };   // script class, select access
	Foo { toThis: bind vf.memHerePro with inverse };   // script class, select access
	Foo { toThis: bind vf.memHerePkg with inverse };   // script class, select access
	Foo { toThis: bind vf.memHereScr with inverse };   // script class, select access

	Foo { toThis: bind vf.memHereIPub with inverse };   // script class, select access
	Foo { toThis: bind vf.memHereIPro with inverse };   // script class, select access
	Foo { toThis: bind vf.memHereIPkg with inverse };   // script class, select access
	Foo { toThis: bind vf.memHereIScr with inverse };   // script class, select access

	Foo { toThis: bind vf.memHereRPub with inverse };   // script class, select access
	Foo { toThis: bind vf.memHereRPro with inverse };   // script class, select access
	Foo { toThis: bind vf.memHereRPkg with inverse };   // script class, select access
	Foo { toThis: bind vf.memHereRScr with inverse };   // script class, select access

	Foo { toThis: bind vf.memHereIRPub with inverse };   // script class, select access
	Foo { toThis: bind vf.memHereIRPro with inverse };   // script class, select access
	Foo { toThis: bind vf.memHereIRPkg with inverse };   // script class, select access
	Foo { toThis: bind vf.memHereIRScr with inverse };   // script class, select access
}

function memFooInvBind() {
	var vf = Foo {};
	Foo { toThis: bind vf.fooPub with inverse };   // nested class
	Foo { toThis: bind vf.fooPro with inverse };   // nested class
	Foo { toThis: bind vf.fooPkg with inverse };   // nested class
	Foo { toThis: bind vf.fooScr with inverse };   // nested class

	Foo { toThis: bind vf.fooIPub with inverse };   // nested class
	Foo { toThis: bind vf.fooIPro with inverse };   // nested class
	Foo { toThis: bind vf.fooIPkg with inverse };   // nested class
	Foo { toThis: bind vf.fooIScr with inverse };   // nested class

	Foo { toThis: bind vf.fooRPub with inverse };   // nested class
	Foo { toThis: bind vf.fooRPro with inverse };   // nested class
	Foo { toThis: bind vf.fooRPkg with inverse };   // nested class
	Foo { toThis: bind vf.fooRScr with inverse };   // nested class

	Foo { toThis: bind vf.fooIRPub with inverse };   // nested class
	Foo { toThis: bind vf.fooIRPro with inverse };   // nested class
	Foo { toThis: bind vf.fooIRPkg with inverse };   // nested class
	Foo { toThis: bind vf.fooIRScr with inverse };   // nested class
}

function memSubInvBind() {
	var vf = jfxc1250subFail {};
	Foo { toThis: bind vf.memPub with inverse };   // same package
	Foo { toThis: bind vf.memPro with inverse };   // same package
	Foo { toThis: bind vf.memPkg with inverse };   // same package
	Foo { toThis: bind vf.memScr with inverse };   // ERROR: same package

	Foo { toThis: bind vf.memIPub with inverse };   // same package
	Foo { toThis: bind vf.memIPro with inverse };   // same package
	Foo { toThis: bind vf.memIPkg with inverse };   // same package
	Foo { toThis: bind vf.memIScr with inverse };   // ERROR: same package

	Foo { toThis: bind vf.memRPub with inverse };   // same package
	Foo { toThis: bind vf.memRPro with inverse };   // same package
	Foo { toThis: bind vf.memRPkg with inverse };   // same package
	Foo { toThis: bind vf.memRScr with inverse };   // ERROR: same package

	Foo { toThis: bind vf.memIRPub with inverse };   // same package
	Foo { toThis: bind vf.memIRPro with inverse };   // same package
	Foo { toThis: bind vf.memIRPkg with inverse };   // same package
	Foo { toThis: bind vf.memIRScr with inverse };   // ERROR: same package
}

function memSubAInvBind() {
	var vf = jfxc1250subFailA {};
	Foo { toThis: bind vf.memPub with inverse };   // different package
	Foo { toThis: bind vf.memPro with inverse };   // ERROR: different package
	Foo { toThis: bind vf.memPkg with inverse };   // ERROR: different package
	Foo { toThis: bind vf.memScr with inverse };   // ERROR: different package

	Foo { toThis: bind vf.memIPub with inverse };   // different package
	Foo { toThis: bind vf.memIPro with inverse };   // ERROR: different package
	Foo { toThis: bind vf.memIPkg with inverse };   // ERROR: different package
	Foo { toThis: bind vf.memIScr with inverse };   // ERROR: different package

	Foo { toThis: bind vf.memRPub with inverse };   // different package
	Foo { toThis: bind vf.memRPro with inverse };   // ERROR: different package
	Foo { toThis: bind vf.memRPkg with inverse };   // ERROR: different package
	Foo { toThis: bind vf.memRScr with inverse };   // ERROR: different package

	Foo { toThis: bind vf.memIRPub with inverse };   // different package
	Foo { toThis: bind vf.memIRPro with inverse };   // ERROR: different package
	Foo { toThis: bind vf.memIRPkg with inverse };   // ERROR: different package
	Foo { toThis: bind vf.memIRScr with inverse };   // ERROR: different package
}

function run() {
	memInhInvBind();
	memInhAInvBind();
	memHereInvBind();
	memFooInvBind();
	memSubInvBind();
	memSubAInvBind();
}
