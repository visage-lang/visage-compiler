/**
 * Regression test for JFXC-1629 : Enforce public-read modifier
 *
 * @compile jfxc1629One.fx
 * @test/compile-error
 */

var uno = jfxc1629One { twub: 222 };  // should fail
uno.twub = 666; // should fail


