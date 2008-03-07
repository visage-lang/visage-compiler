/* Binding Overhaul test: literals
 *
 * @test
 * @run
 */

import java.lang.System;

var enableBindingOverhaul;

function xxxxx() {
var vi = 5;
var vd = 5.5;
var vb = true;
var vs = "there";

var i = bind 22;
var d = bind 3.14159265368979;
var b = bind false;
var s = bind "hello";

var qi = bind [22, vi];
var qd = bind [3.14159265368979, vd];
var qb = bind [false, vb];
var qs = bind ["hello", vs];
var qn : String[] = null;
var qe : java.lang.Object[] = [];

System.out.println(i);
System.out.println(d);
System.out.println(b);
System.out.println(s);
System.out.println(qi);
System.out.println(qd);
System.out.println(qb);
System.out.println(qs);
System.out.println(qn);
System.out.println(qe);
}

xxxxx()