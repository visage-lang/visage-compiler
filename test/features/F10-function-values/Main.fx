/* Feature test #10 -- function values
 * TODO: reenable when fixed.
 * @run
 */
import java.lang.*;

function plus10 (x : Integer) : Integer { return x+10; }

function call3 (f, prefix) {
  System.err.println("{prefix}: 1->{f(1)} 2->{f(2)} 3->{f(3)}");
}

call3(plus10, "call named plus10");
call3(function(x : Integer) :Integer {x+5}, "call anonymous x+5");

var ff = plus10;
call3(ff, "call plus10 assigned to ff");
ff = function (x : Integer) {x+2};
call3(ff, "call anonymous x+2 assigned to ff");
