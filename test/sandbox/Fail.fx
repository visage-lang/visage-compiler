/*
 * @test this test should fail, in order to verify Hudson build error handling
 */

package foo;
import java.lang.*;
import java.util.ArrayList;

public class Bar {
	readonly attribute a : Integer;
	private attribute b : Integer;
//	function f1(x, y, z);    //TODO reinstate
	public function f2(a : Integer, b: Number, c : Object) : Boolean;
	operation op();
}

var tt = 9;

attribute Bar.a = 3;
attribute Bar.b = bind a * 10;
//function Bar.f1(x, y, z) { return x - y; }    //TODO reinstate
function Bar.f2(a : Integer, b: Number, c : Object) : Boolean { return a + b <> 6; }
operation Bar.op() { 
	if (not (a > 2 and a < 10) or a % 2 == 0) {
		System.out.println("operation"); 
		return;
	}
}
/**x
class Bat extends Bar {
	attribute rr : Bar;
}
attribute Bat.rr = new Bar;  //TODO reinstate: Bar { a : 88 };
x**/

/*** not implemented yet
operation snod(h) : String {
	if (h instanceof ArrayList) {
		return "bass";
	}
	return "hi";
}

function sned(k) {
	return -k;
}
*****/

var v1 : Number = 44.44; //TODO reinstate no   = 44.44
var v2 : Integer = tt * tt / 5;
var v3 = bind v2;
var v4 : Number = bind lazy v1;
v1 = 6.7;
v1 += v2;

//var bat = Bat { a: 12 };
//bat.rr.f1(2, 3, 4);    //TODO reinstate
