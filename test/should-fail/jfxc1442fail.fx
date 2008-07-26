/**
 * Regression test for JFXC-1442 : Allow access modifiers on unit-level (aka file/module-level) variables
 *
 * @test/compile-error
 * @compilefirst jfxc1442sub.fx
 */

jfxc1442sub.pub = 8;
jfxc1442sub.priv = 8;
jfxc1442sub.read = 8;
jfxc1442sub.readpriv = 8;
var x = readpriv.read + readpriv.readpriv;
jfxc1442sub.myvar = 8;

