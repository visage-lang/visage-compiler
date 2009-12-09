/**
 * Regression test JFXC-3586 : Compiled bind: bound sequence: for-expression
 *
 * Primative induction var. Object element value. Update.
 *
 * @test
 * @run
 */

var a = 0;
var b = 10;
def bf = bind for (x in [a..b]) { "@@@{x}@@@" };
a = 1; // Updates before first access
b = 5;
println(bf);
a = 3;
println(bf);
b = 7;
println(bf);
a = 10;
println(bf);

