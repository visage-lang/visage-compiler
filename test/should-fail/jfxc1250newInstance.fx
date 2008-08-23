/**
 * Regression test JFXC-1250 : visibility modifiers
 *
 * New instance component
 *
 * @compilefirst jfxc1250/a/jfxc1250subFailA.fx
 * @compilefirst jfxc1250subFail.fx
 * @test/compile-error
 */
import java.lang.System;
import jfxc1250.a.jfxc1250subFailA;

public class cPub {}
package class cPkg {}
class cScr {}

function cNew() {
	var x1 = cPub {};
	var x2 = cPkg {};
	var x3 = cScr {};
}

function cVisNew() {
	var x1 = jfxc1250newInstance.cPub {};
	var x2 = jfxc1250newInstance.cPkg {};
	var x3 = jfxc1250newInstance.cScr {};
}

function cSubNew() {
	var x1 = jfxc1250subFail.cPub {};
	var x2 = jfxc1250subFail.cPkg {};
	var x3 = jfxc1250subFail.cScr {};
}

function cSubANew() {
	var x1 = jfxc1250subFailA.cPub {};
	var x2 = jfxc1250subFailA.cPkg {};
	var x3 = jfxc1250subFailA.cScr {};
}

function run( ) {
	cNew();
	cVisNew();
	cSubNew();
	cSubANew();
}