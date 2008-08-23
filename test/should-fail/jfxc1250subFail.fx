/**
 * @subtest
 */

public var slPub = 0;
package var slPkg = 0;
var slScr = 0;

public-init public var slIPub = 0;
public-init package var slIPkg = 0;
public-init var slIScr = 0;

public-read public var slRPub = 0;
public-read package var slRPkg = 0;
public-read var slRScr = 0;

public-init public-read public var slIRPub = 0;
public-init public-read package var slIRPkg = 0;
public-init public-read var slIRScr = 0;

public function slFPub() { 0 }
package function slFPkg() { 0 }
function slFScr() { 0 }

public class cPub {}
package class cPkg {}
class cScr {}

package class jfxc1250subFail {
	public var memPub = 0;
	protected var memPro = 0;
	package var memPkg = 0;
	var memScr = 0;

	public-init public var memIPub = 0;
	public-init protected var memIPro = 0;
	public-init package var memIPkg = 0;
	public-init var memIScr = 0;

	public-read public var memRPub = 0;
	public-read protected var memRPro = 0;
	public-read package var memRPkg = 0;
	public-read var memRScr = 0;

	public-init public-read public var memIRPub = 0;
	public-init public-read protected var memIRPro = 0;
	public-init public-read package var memIRPkg = 0;
	public-init public-read var memIRScr = 0;

	public function memFPub() { 0 }
	protected function memFPro() { 0 }
	package function memFPkg() { 0 }
	function memFScr() { 0 }
}

