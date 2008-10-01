/**
 * Regression test JFXC-2110 : 
 *
 * @compilefirst jfxc2110Color.fx
 * @test
 */

var a = 3;
var b  = bind if (a < 2) jfxc2110Color {} else jfxc2110Color.RED;
