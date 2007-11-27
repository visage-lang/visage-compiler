/*
 * Regression test: for JFXC-5 : Types not determined transitively.
 *
 * @test
 */
var x; 
var y = x; 
x = 9; 
var z = y + y; 
 
