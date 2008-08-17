/**
 * @subtest
 */
package jfxc1250.a;

public var slPub = 0;
package var slPkg = 0;
var slScr = 0;
non-writable public var slNPub = 0;
non-writable package var slNPkg = 0;
non-writable var slNScr = 0;
public-readable package var slRPkg = 0;
public-readable var slRScr = 0;
non-writable public-readable package var slNRPkg = 0;
non-writable public-readable var slNRScr = 0;

public function slFPub() { 0 }
package function slFPkg() { 0 }
function slFScr() { 0 }

public class cPub {}
package class cPkg {}
class cScr {}

public class jfxc1250subFailA {
	public var memPub = 0;
	protected var memPro = 0;
	package var memPkg = 0;
	var memScr = 0;
	non-writable public var memNPub = 0;
	non-writable protected var memNPro = 0;
	non-writable package var memNPkg = 0;
	non-writable var memNScr = 0;
	public-readable protected var memRPro = 0;
	public-readable package var memRPkg = 0;
	public-readable var memRScr = 0;
	non-writable public-readable protected var memNRPro = 0;
	non-writable public-readable package var memNRPkg = 0;
	non-writable public-readable var memNRScr = 0;

	public function memFPub() { 0 }
	protected function memFPro() { 0 }
	package function memFPkg() { 0 }
	function memFScr() { 0 }
}

