/**
 * Regression test JFXC-3627 : Compiled bind: bound sequence: function invocation
 *
 * @test
 * @run
 */

var thing : Object[] = [1];
def cn : Object[] = bind foo(thing) on replace {println("Hello! {cn}"); }
thing = [44];
function foo(x:Object[]):Object[] { return x;}
