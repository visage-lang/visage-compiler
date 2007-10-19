/* Feature test #10 -- function values
 * @test
 */
import java.lang.*;

function plus10 (x : Integer) : Integer { return x+10; }
//System.err.println(plus10);

function call3 (f : function(Integer)String, prefix) {
  System.err.println("{prefix}: 1->{f(1)} 2->{f(2)} 3->{f(3)}");
}

class Cl1 {
  attribute fvar : function(String)String;
  attribute xvar : String;
};
var cl = new Cl1();
cl.fvar(cl.xvar);

/* FIXME - not yet working
call3(plus10, "call named plus10");
call3(function(x : Integer) :Integer {x+5}, "call anonymous x+5");

var ff = plus10;
call3(ff, "call plus10 assigned to ff");
ff = function (x : Integer) {x+2};
call3(ff, "call anonymous x+2 assigned to ff");
*/

function fun0a (fparg : function()java.lang.Double) : Void {
  var gparg : function()java.lang.Double = fparg;
}
function fun0b (fparg : function()java.lang.Double) : Void {
  var gparg : function()java.lang.Number = fparg;
}
function fun2a (fparg : function(java.lang.Number, Integer)java.lang.Double) : Void {
  var gparg : function(java.lang.Double, Integer)java.lang.Double = fparg;
}
function fun2b (fparg : function(java.lang.Number, Integer)java.lang.Double) : Void {
  var gparg : function(java.lang.Double, Integer)java.lang.Double = fparg;
}

/* These should be compile-time errors:, but we don't support
 * error tests yet. FIXME
function fun0c (fparg : function()java.lang.Number) : Void {
  var gparg : function()java.lang.Double = fparg;
}
function fun2c (fparg : function(java.lang.Double, Integer)java.lang.Double) : Void {
  var gparg : function(java.lang.Number, Integer)java.lang.Double = fparg;
}
*/
