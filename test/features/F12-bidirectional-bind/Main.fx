/* Feature test #12 - bidirectional var binding
 * Demonstrates: bidirectional binding
 * @test
 * @run
 */

import java.lang.System;
 
class AttHold {
    var a : Integer = 14;
    var b : Integer = bind a with inverse;
}
var label = "far";
var boundLabel = bind label with inverse;

System.out.println("{label} == {boundLabel}");

label = "near";
System.out.println("{label} == {boundLabel}");

boundLabel = "there";
System.out.println("{label} == {boundLabel}");

var ah = new AttHold;

System.out.println("{ah.a} == {ah.b}");

ah.a = 3;
System.out.println("{ah.a} == {ah.b}");

ah.b = 99;
System.out.println("{ah.a} == {ah.b}");

var tr = 77;

ah = AttHold {
    a: bind tr with inverse
};

System.out.println("{ah.a} == {tr}");

ah.a = 6;
System.out.println("{ah.a} == {tr}");

tr = 11111;
System.out.println("{ah.a} == {tr}");

