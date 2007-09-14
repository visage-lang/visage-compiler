/* Feature test #12 - bidirectional attribute binding
 * Demonstrates: bidirectional binding
 * @test
 * @run
 */

import java.lang.System;

class AttHold {
    attribute a : Integer = 14;
    attribute b : Integer = bind a with inverse;
}
var label = "far";
var bound = bind label with inverse;

System.out.println("{label} == {bound}");

label = "near";
System.out.println("{label} == {bound}");

bound = "there";
System.out.println("{label} == {bound}");

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

tr = 1111;
System.out.println("{ah.a} == {tr}");

