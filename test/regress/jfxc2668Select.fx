/**
 * JFXC-2668 : Regression: crash: no bound conversion of instance to sequence of superclass 
 *
 * @compilefirst jfxc2646sub.fx
 * @test
 * @run
 */

var a = jfxc2646sub {};
var zzz : Object[] = bind a.x;
println(zzz);
