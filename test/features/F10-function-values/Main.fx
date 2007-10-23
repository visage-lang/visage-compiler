/* Feature test #10 -- function values
 * @test
 * @run
 */
import java.lang.*;

function plus10 (x : Integer) : Integer { return x+10; }
//System.err.println(plus10);

function call3 (f : function(:Integer):String, prefix) {
  System.err.println("{prefix}: 1->{f(1)} 2->{f(2)} 3->{f(3)}");
}

var action1 : function(:String):String
   = function(x:String):String { System.out.println("button pressed"); x };
System.out.println(action1("action1 called"));

class Cl1 {
  attribute fvar : function(:String):String;
  attribute xvar : String;
  attribute concat = function (y : String) { "{xvar}-{y}" };
};
var cl = new Cl1();
cl.xvar = "cl.fvar called";
cl.fvar = action1;
System.out.println(cl.fvar(cl.xvar));
cl.xvar = "reset cl.xvar";
System.out.println(cl.concat("Cl1.concat called"));

/* FIXME - not yet working
Expected output:
call named plus10: 1->11 2->12 3->13
call anonymous x+5: 1->6 2->7 3->8
call plus10 assigned to ff: 1->11 2->12 3->13
call anonymous x+2 assigned to ff: 1->3 2->4 3->5

call3(plus10, "call named plus10");
call3(function(x : Integer) :Integer {x+5}, "call anonymous x+5");

var ff = plus10;
call3(ff, "call plus10 assigned to ff");
ff = function (x : Integer) {x+2};
call3(ff, "call anonymous x+2 assigned to ff");
*/

function fun0a (fparg : function():java.lang.Double) : Void {
  var gparg : function():java.lang.Double = fparg;
}
function fun0b (fparg : function():java.lang.Double) : Void {
  var gparg : function():java.lang.Number = fparg;
}
function fun2a (fparg : function(:java.lang.Number, :Integer):java.lang.Double) : Void {
  var gparg : function(:java.lang.Double, :Integer):java.lang.Double = fparg;
}
function fun2b (fparg : function(:java.lang.Number, :Integer):java.lang.Double) : Void {
  var gparg : function(:java.lang.Double, :Integer):java.lang.Double = fparg;
}

/* These should be compile-time errors:, but we don't support
 * error tests yet. FIXME
function fun0c (fparg : function():java.lang.Number) : Void {
  var gparg : function()java.lang.Double = fparg;
}
function fun2c (fparg : function(:java.lang.Double, :Integer):java.lang.Double) : Void {
  var gparg : function(java.lang.Number, Integer)java.lang.Double = fparg;
}
*/
