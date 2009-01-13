/**
 * JFXC-2646 : Franca: CutePlayer2 compilation failed (Regression)
 *
 * @compilefirst jfxc2646sub.fx
 * @test
 * @run
 */

var a = jfxc2646sub {};
var k = bind a.x;
println(k);

