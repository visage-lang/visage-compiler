/*
 * Regression: JFXC-2341 - Capture of overloaded java instance methods into function values not rejected.
 *
 * @test
 * @run
 *
 */

var pl = java.lang.System.out.println;
pl("string");
