/**
 * Regression test for JFXC-1442 : Allow access modifiers on script-level variables
 *
 * @test/compile-error
 * @compilefirst jfxc1442sub.fx
 */

jfxc1442sub.pub = 8;
jfxc1442sub.read = 8;
var z = jfxc1442sub.read;
//var x = jfxc1442sub.myvar;
//jfxc1442sub.myvar = 8;

