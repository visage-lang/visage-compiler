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

public class jfxc1250visBlank {
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

function main() {
/////
var sv = jfxc1250visBlank {};
System.out.println(jfxc1250visBlank.slPub);
System.out.println(jfxc1250visBlank.slPkg);
System.out.println(jfxc1250visBlank.slScr);
System.out.println(jfxc1250visBlank.slNPub);
System.out.println(jfxc1250visBlank.slNPkg);
System.out.println(jfxc1250visBlank.slNScr);
System.out.println(jfxc1250visBlank.slRPkg);
System.out.println(jfxc1250visBlank.slRScr);
System.out.println(jfxc1250visBlank.slNRPkg);
System.out.println(jfxc1250visBlank.slNRScr);

System.out.println(sv.memPub);
System.out.println(sv.memPkg);
System.out.println(sv.memNPub);
System.out.println(sv.memNPkg);
System.out.println(sv.memRPkg);
System.out.println(sv.memRScr);
System.out.println(sv.memNRPkg);
System.out.println(sv.memNRScr);

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
System.out.println(jfxc1250subBlank.slNPub);
System.out.println(jfxc1250subBlank.slNPkg);
System.out.println(jfxc1250subBlank.slRPkg);
System.out.println(jfxc1250subBlank.slRScr);
System.out.println(jfxc1250subBlank.slNRPkg);
System.out.println(jfxc1250subBlank.slNRScr);

System.out.println(s.memPub);
System.out.println(s.memPkg);
System.out.println(s.memNPub);
System.out.println(s.memNPkg);
System.out.println(s.memRPkg);
System.out.println(s.memRScr);
System.out.println(s.memNRPkg);
System.out.println(s.memNRScr);

System.out.println(jfxc1250subBlank.slFPub());
System.out.println(jfxc1250subBlank.slFPkg());

System.out.println(s.memFPub());
System.out.println(s.memFPkg());

var z1 = jfxc1250subBlank.cPub {};
var z2 = jfxc1250subBlank.cPkg {};

/////
var sa = jfxc1250subA {};
System.out.println(jfxc1250subA.slPub);
System.out.println(jfxc1250subA.slNPub);
System.out.println(jfxc1250subA.slRPkg);
System.out.println(jfxc1250subA.slRScr);
System.out.println(jfxc1250subA.slNRPkg);
System.out.println(jfxc1250subA.slNRScr);

System.out.println(sa.memPub);
System.out.println(sa.memNPub);
System.out.println(sa.memRPkg);
System.out.println(sa.memRScr);
System.out.println(sa.memNRPkg);
System.out.println(sa.memNRScr);

System.out.println(jfxc1250subA.slFPub());

System.out.println(sa.memFPub());

var y1 = jfxc1250subA.cPub {};
}