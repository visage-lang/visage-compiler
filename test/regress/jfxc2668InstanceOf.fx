/**
 * JFXC-2668 : Regression: crash: no bound conversion of instance to sequence of superclass 
 *
 * @test
 * @run
 */

var k = jfxc2668InstanceOf{};
var zzz : Object[] = bind k instanceof jfxc2668InstanceOf;
println(zzz);
