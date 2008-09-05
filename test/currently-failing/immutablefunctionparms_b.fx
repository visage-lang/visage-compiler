/*
 * function parameters are immutable, see jfxc-1106
 * Are sequence parameter elements immutable? see jfxc1825
 *
 * @test/fail
 *
 * NOTE: This currently compiles but the bug contention is that maybe it should not.
 * If bug resolution is to leave as is, this test can be put in as a successful
 * regression test, so that if things change, its failures will be an alert,or
 * it can be removed altogether.
 */

function f( p1:Integer, p2:String, ps1:Integer[], ps2:String[]) {
ps1[0] = 99;
ps2[0] = "oops!";
}

f(0,"fdsa",[1,2,3], ["Hello", ", ","World","!"]);
