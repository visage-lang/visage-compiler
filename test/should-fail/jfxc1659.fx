/**
 * Regression test for JFXC-1659 : Crash in attribution for variable declaration within an expression
 *
 * @test/fail
 */

var x;
x = (def y = 8)
