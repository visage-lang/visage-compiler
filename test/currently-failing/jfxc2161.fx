/*
 * @test
 * This should be a run test with no output, or it needs a "PASS" output in EXEPECTED file.
 *
 * This bug has two parts, the need for a warning and the bug itself:
 * jfxc-2166: This should also at least generate a possible loss of precision warning.
 * jfxc-2167 is the bug part of 2161: "need to fix translation of INT*=NUMBER"
 */
var x=10;
x*= 0.5;
if( x==0) {
println("var x=10;");
println("x*= 0.5;");
println("ERROR: x!=5 or 5.0;  x = {x}");
}
