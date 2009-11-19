/*
 * Regression test for JFXC-3678 : Compiled-bind: isInitialized fails with mixins.
 *
 * @compile jfxc3678M.fx
 * @test
 * @run
 */

class C extends jfxc3678M {} 
function f(c:C) { isInitialized(c.x); } 

var y;
var c = new C();

println(f(c));
println(isInitialized(y));

