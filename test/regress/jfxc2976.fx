/*
 * Regression: JFXC-2976 - Compiler crash: floatVar instanceof java.lang.Integer.
 *
 * @test
 * @run
 *
 */

def num = 1031.1;
println(num instanceof Integer); 
