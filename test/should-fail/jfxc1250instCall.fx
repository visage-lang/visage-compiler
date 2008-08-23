/**
 * Regression test JFXC-1250 : visibility modifiers
 *
 *     instance function call
 *
 * @compilefirst jfxc1250/a/jfxc1250subFailA.fx
 * @compilefirst jfxc1250subFail.fx
 * @test/compile-error
 */

import java.lang.System;
import jfxc1250.a.jfxc1250subFailA;

public class jfxc1250instCall {
	public function memHereFPub() { 3 }
	protected function memHereFPro() { 3 }
	package function memHereFPkg() { 3 }
	function memHereFScr() { 3 }
}

class Foo {
	public function fooFPub() { 3 }
	protected function fooFPro() { 3 }
	package function fooFPkg() { 3 }
	function fooFScr() { 3 }
}

class Inh extends jfxc1250subFail {
    function memCall() {
	System.out.println( memFPub() );   // inherited, same package
	System.out.println( memFPro() );   // inherited, same package
	System.out.println( memFPkg() );   // inherited, same package
	System.out.println( memFScr() );   // ERROR: inherited, same package
    }
}

class InhA extends jfxc1250subFailA {
    function memCall() {
	System.out.println( memFPub() );   // inherited, different package
	System.out.println( memFPro() );   // inherited, different package
	System.out.println( memFPkg() );   // ERROR: inherited, different package
	System.out.println( memFScr() );   // ERROR: inherited, different package
    }
}

function memInhCall() {
	var vf = Inh {};
	vf.memCall();
}

function memInhACall() {
	var vf = InhA {};
	vf.memCall();
}

function memHereCall() {
	var vf = jfxc1250instCall {};
	System.out.println( vf.memHereFPub() );   // script class, select access
	System.out.println( vf.memHereFPro() );   // script class, select access
	System.out.println( vf.memHereFPkg() );   // script class, select access
	System.out.println( vf.memHereFScr() );   // script class, select access
}

function memFooCall() {
	var vf = Foo {};
	System.out.println( vf.fooFPub() );   // nested class
	System.out.println( vf.fooFPro() );   // nested class
	System.out.println( vf.fooFPkg() );   // nested class
	System.out.println( vf.fooFScr() );   // nested class
}

function memSubCall() {
	var vf = jfxc1250subFail {};
	System.out.println( vf.memFPub() );   // same package
	System.out.println( vf.memFPro() );   // same package
	System.out.println( vf.memFPkg() );   // same package
	System.out.println( vf.memFScr() );   // ERROR: same package
}

function memSubACall() {
	var vf = jfxc1250subFailA {};
	System.out.println( vf.memFPub() );   // different package
	System.out.println( vf.memFPro() );   // ERROR: different package
	System.out.println( vf.memFPkg() );   // ERROR: different package
	System.out.println( vf.memFScr() );   // ERROR: different package
}

function run( ) {
	memInhCall();
	memInhACall();
	memHereCall();
	memFooCall();
	memSubCall();
	memSubACall();
}