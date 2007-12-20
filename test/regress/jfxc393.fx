/*
 * Regression test: JFXC-393:functions aren't checked for return -- CPU goes to 100%
 *
 * @test
 * @compile/fail jfxc393.fx
 */
function foo() : Integer {} 

var bar = foo(); 
