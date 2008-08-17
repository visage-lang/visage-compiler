/**
 * Regression test JFXC-1250 : visibility modifiers
 *
 * @compilefirst jfxc1250/a/jfxc1250subFailA.fx
 * @compilefirst jfxc1250subFail.fx
 * @test/fail
 */
import java.lang.System;
import jfxc1250.a.jfxc1250subFailA;

public var slPub = 3;
package var slPkg = 3;
var slScr = 3;
non-writable public var slNPub = 3;
non-writable package var slNPkg = 3;
non-writable var slNScr = 3;
public-readable package var slRPkg = 3;
public-readable var slRScr = 3;
non-writable public-readable package var slNRPkg = 3;
non-writable public-readable var slNRScr = 3;

public function slFPub() { 3 }
package function slFPkg() { 3 }
function slFScr() { 3 }

public class cPub {}
package class cPkg {}
class cScr {}

public class jfxc1250visFail {
	public var memPub = 3;
	protected var memPro = 3;
	package var memPkg = 3;
	var memScr = 3;
	non-writable public var memNPub = 3;
	non-writable protected var memNPro = 3;
	non-writable package var memNPkg = 3;
	non-writable var memNScr = 3;
	public-readable protected var memRPro = 3;
	public-readable package var memRPkg = 3;
	public-readable var memRScr = 3;
	non-writable public-readable protected var memNRPro = 3;
	non-writable public-readable package var memNRPkg = 3;
	non-writable public-readable var memNRScr = 3;

	public function memFPub() { 3 }
	protected function memFPro() { 3 }
	package function memFPkg() { 3 }
	function memFScr() { 3 }
}

class Foo {
	public var fooPub = 3;
	protected var fooPro = 3;
	package var fooPkg = 3;
	var fooScr = 3;
	non-writable public var fooNPub = 3;
	non-writable protected var fooNPro = 3;
	non-writable package var fooNPkg = 3;
	non-writable var fooNScr = 3;
	public-readable protected var fooRPro = 3;
	public-readable package var fooRPkg = 3;
	public-readable var fooRScr = 3;
	non-writable public-readable protected var fooNRPro = 3;
	non-writable public-readable package var fooNRPkg = 3;
	non-writable public-readable var fooNRScr = 3;

	public function fooFPub() { 3 }
	protected function fooFPro() { 3 }
	package function fooFPkg() { 3 }
	function fooFScr() { 3 }
}

function slVisRead() {
	System.out.println(jfxc1250visFail.slPub);
	System.out.println(jfxc1250visFail.slPkg);
	System.out.println(jfxc1250visFail.slScr);
	System.out.println(jfxc1250visFail.slNPub);
	System.out.println(jfxc1250visFail.slNPkg);
	System.out.println(jfxc1250visFail.slNScr);
	System.out.println(jfxc1250visFail.slRPkg);
	System.out.println(jfxc1250visFail.slRScr);
	System.out.println(jfxc1250visFail.slNRPkg);
	System.out.println(jfxc1250visFail.slNRScr);
}

function memVisRead() {
	var vf = jfxc1250visFail {};
	System.out.println(vf.memPub);
	System.out.println(vf.memPkg);
	System.out.println(vf.memPro);
	System.out.println(vf.memScr);
	System.out.println(vf.memNPub);
	System.out.println(vf.memNPkg);
	System.out.println(vf.memNPro);
	System.out.println(vf.memNScr);
	System.out.println(vf.memRPro);
	System.out.println(vf.memRPkg);
	System.out.println(vf.memRScr);
	System.out.println(vf.memNRPro);
	System.out.println(vf.memNRPkg);
	System.out.println(vf.memNRScr);
}

function fooVisRead() {
	var fv = Foo {};
	System.out.println(fv.fooPub);
	System.out.println(fv.fooPkg);
	System.out.println(fv.fooPro);
	System.out.println(fv.fooScr);
	System.out.println(fv.fooNPub);
	System.out.println(fv.fooNPkg);
	System.out.println(fv.fooNPro);
	System.out.println(fv.fooNScr);
	System.out.println(fv.fooRPro);
	System.out.println(fv.fooRPkg);
	System.out.println(fv.fooRScr);
	System.out.println(fv.fooNRPro);
	System.out.println(fv.fooNRPkg);
	System.out.println(fv.fooNRScr);
}

function slVisCall() {
	System.out.println(jfxc1250visFail.slFPub());
	System.out.println(jfxc1250visFail.slFPkg());
	System.out.println(jfxc1250visFail.slFScr());
}

function memVisCall() {
	var vf = jfxc1250visFail {};
	System.out.println(vf.memFPub());
	System.out.println(vf.memFPkg());
	System.out.println(vf.memFScr());
}

function cVisNew() {
	var x1 = jfxc1250visFail.cPub {};
	var x2 = jfxc1250visFail.cPkg {};
	var x3 = jfxc1250visFail.cScr {};
}

// ---

function slSubRead() {
	System.out.println(jfxc1250subFail.slPub);
	System.out.println(jfxc1250subFail.slPkg);
	System.out.println(jfxc1250subFail.slScr);
	System.out.println(jfxc1250subFail.slNPub);
	System.out.println(jfxc1250subFail.slNPkg);
	System.out.println(jfxc1250subFail.slNScr);
	System.out.println(jfxc1250subFail.slRPkg);
	System.out.println(jfxc1250subFail.slRScr);
	System.out.println(jfxc1250subFail.slNRPkg);
	System.out.println(jfxc1250subFail.slNRScr);
}

function memSubRead() {
	var sf = jfxc1250subFail {};
	System.out.println(sf.memPub);
	System.out.println(sf.memPkg);
	System.out.println(sf.memPro);
	System.out.println(sf.memScr);
	System.out.println(sf.memNPub);
	System.out.println(sf.memNPkg);
	System.out.println(sf.memNPro);
	System.out.println(sf.memNScr);
	System.out.println(sf.memRPro);
	System.out.println(sf.memRPkg);
	System.out.println(sf.memRScr);
	System.out.println(sf.memNRPro);
	System.out.println(sf.memNRPkg);
	System.out.println(sf.memNRScr);
}

function slSubCall() {
	System.out.println(jfxc1250subFail.slFPub());
	System.out.println(jfxc1250subFail.slFPkg());
	System.out.println(jfxc1250subFail.slFScr());
}

function memSubCall() {
	var sf = jfxc1250subFail {};
	System.out.println(sf.memFPub());
	System.out.println(sf.memFPkg());
	System.out.println(sf.memFScr());
}

function cSubNew() {
	var x1 = jfxc1250subFail.cPub {};
	var x2 = jfxc1250subFail.cPkg {};
	var x3 = jfxc1250subFail.cScr {};
}

//---

function slARead() {
	System.out.println(jfxc1250subFailA.slPub);
	System.out.println(jfxc1250subFailA.slPkg);
	System.out.println(jfxc1250subFailA.slScr);
	System.out.println(jfxc1250subFailA.slNPub);
	System.out.println(jfxc1250subFailA.slNPkg);
	System.out.println(jfxc1250subFailA.slNScr);
	System.out.println(jfxc1250subFailA.slRPkg);
	System.out.println(jfxc1250subFailA.slRScr);
	System.out.println(jfxc1250subFailA.slNRPkg);
	System.out.println(jfxc1250subFailA.slNRScr);
}

function memARead() {
	var sfa = jfxc1250subFailA {};
	System.out.println(sfa.memPub);
	System.out.println(sfa.memPkg);
	System.out.println(sfa.memPro);
	System.out.println(sfa.memScr);
	System.out.println(sfa.memNPub);
	System.out.println(sfa.memNPkg);
	System.out.println(sfa.memNPro);
	System.out.println(sfa.memNScr);
	System.out.println(sfa.memRPro);
	System.out.println(sfa.memRPkg);
	System.out.println(sfa.memRScr);
	System.out.println(sfa.memNRPro);
	System.out.println(sfa.memNRPkg);
	System.out.println(sfa.memNRScr);
}

function slACall() {
	System.out.println(jfxc1250subFailA.slFPub());
	System.out.println(jfxc1250subFailA.slFPkg());
	System.out.println(jfxc1250subFailA.slFScr());
}

function memACall() {
	var sfa = jfxc1250subFailA {};
	System.out.println(sfa.memFPub());
	System.out.println(sfa.memFPkg());
	System.out.println(sfa.memFScr());
}

function cANew() {
	var x1 = jfxc1250subFailA.cPub {};
	var x2 = jfxc1250subFailA.cPkg {};
	var x3 = jfxc1250subFailA.cScr {};
}


function run( ) {
	slVisRead();
	memVisRead();
	fooVisRead();
	slVisCall();
	memVisCall();
	cVisNew();

	slSubRead();
	memSubRead();
	slSubCall();
	memSubCall();
	cSubNew();

	slARead();
	memARead();
	slACall();
	memACall();
	cANew();
}