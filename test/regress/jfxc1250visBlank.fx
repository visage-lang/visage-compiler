/**
 * Regression test JFXC-1250 : visibility modifiers
 *
 * @compilefirst jfxc1250/a/jfxc1250subA.fx
 * @compilefirst jfxc1250subBlank.fx
 * @test
 * @run
 */
import java.lang.System;
import jfxc1250.a.jfxc1250subA;

public var slPub = 3;
package var slPkg = 3;
var slScr = 3;

public-read package var slRPkg = 3;
public-read var slRScr = 3;

public function slFPub() { 3 }
package function slFPkg() { 3 }
function slFScr() { 3 }

public class cPub {}
package class cPkg {}
class cScr {}

public class jfxc1250visBlank {
	public var memPub = 3;
	protected var memPro = 3;
	package var memPkg = 3;
	var memScr = 3;

	public-init protected var memNPro = 3;
	public-init package var memNPkg = 3;
	public-init var memNScr = 3;

	public-read protected var memRPro = 3;
	public-read package var memRPkg = 3;
	public-read var memRScr = 3;

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

	public-init protected var fooNPro = 3;
	public-init package var fooNPkg = 3;
	public-init var fooNScr = 3;

	public-read protected var fooRPro = 3;
	public-read package var fooRPkg = 3;
	public-read var fooRScr = 3;

	public function fooFPub() { 3 }
	protected function fooFPro() { 3 }
	package function fooFPkg() { 3 }
	function fooFScr() { 3 }
}

function run( ) {
/////
var sv = jfxc1250visBlank {};
var fv = Foo {};
System.out.println(jfxc1250visBlank.slPub);
System.out.println(jfxc1250visBlank.slPkg);
System.out.println(jfxc1250visBlank.slScr);
System.out.println(jfxc1250visBlank.slRPkg);
System.out.println(jfxc1250visBlank.slRScr);

System.out.println(sv.memPub);
System.out.println(sv.memPkg);
System.out.println(sv.memPro);
System.out.println(sv.memScr);
System.out.println(sv.memNPkg);
System.out.println(sv.memNPro);
System.out.println(sv.memNScr);
System.out.println(sv.memRPro);
System.out.println(sv.memRPkg);
System.out.println(sv.memRScr);

System.out.println(fv.fooPub);
System.out.println(fv.fooPkg);
System.out.println(fv.fooPro);
System.out.println(fv.fooScr);
System.out.println(fv.fooNPkg);
System.out.println(fv.fooNPro);
System.out.println(fv.fooNScr);
System.out.println(fv.fooRPro);
System.out.println(fv.fooRPkg);
System.out.println(fv.fooRScr);

System.out.println(jfxc1250visBlank.slFPub());
System.out.println(jfxc1250visBlank.slFPkg());
System.out.println(jfxc1250visBlank.slFScr());

System.out.println(sv.memFPub());
System.out.println(sv.memFPkg());
System.out.println(sv.memFScr());

var x1 = jfxc1250visBlank.cPub {};
var x2 = jfxc1250visBlank.cPkg {};
var x3 = jfxc1250visBlank.cScr {};

/////
var s = jfxc1250subBlank {};
System.out.println(jfxc1250subBlank.slPub);
System.out.println(jfxc1250subBlank.slPkg);
System.out.println(jfxc1250subBlank.slRPkg);
System.out.println(jfxc1250subBlank.slRScr);

System.out.println(s.memPub);
System.out.println(s.memPkg);
System.out.println(s.memNPkg);
System.out.println(s.memNScr);
System.out.println(s.memRPkg);
System.out.println(s.memRScr);

System.out.println(jfxc1250subBlank.slFPub());
System.out.println(jfxc1250subBlank.slFPkg());

System.out.println(s.memFPub());
System.out.println(s.memFPkg());

var z1 = jfxc1250subBlank.cPub {};
var z2 = jfxc1250subBlank.cPkg {};

/////
var sa = jfxc1250subA {};
System.out.println(jfxc1250subA.slPub);
System.out.println(jfxc1250subA.slRPkg);
System.out.println(jfxc1250subA.slRScr);

System.out.println(sa.memPub);
System.out.println(sa.memRPkg);
System.out.println(sa.memRScr);

System.out.println(jfxc1250subA.slFPub());

System.out.println(sa.memFPub());

var y1 = jfxc1250subA.cPub {};
}
