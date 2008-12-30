/*
 * Assigning compatible values to a variable of the type Byte expecting
 * a warning about the presicion loss.
 *
 * @compilefirst Values.fx
 * @test/warning
 */

var b : Byte;

b = Values.cc;
b = Values.ff;
b = Values.dd;
b = Values.nn;
// A compiler warning is not generated for the below conversions.
// An issue JFXC-2584 is created. This test will start failing after it is fixed.
b = Values.ii;
b = Values.ll;
b = Values.ss;


