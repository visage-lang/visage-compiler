/**
 * Regression test for JFXC-1628 : Enforce 'def' non-assignability across scripts
 *
 * @test/fail
 * @compilefirst jfxc1628b.fx
 */

var x = jfxc1628b {nono: 7};
x.nono = 4;
jfxc1628.sonono = 5;

