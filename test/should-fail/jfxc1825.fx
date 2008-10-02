/*
 * function parameters are immutable, see jfxc-1106. Yes.
 * Are sequence parameter elements immutable? see jfxc1825. Yes.
 *
 * Also, see JFXC-1998.
 *
 * @test/compile-error
 *
 */

function f( p1:Integer, p2:String, ps1:Integer[], ps2:String[]) {
ps1[0] = 99;
ps2[0] = "oops!";
}

f(0,"fdsa",[1,2,3], ["Hello", ", ","World","!"]);
