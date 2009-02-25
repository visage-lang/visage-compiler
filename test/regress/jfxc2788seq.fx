/**
 * JFXC-2788 : Collapse bound conditional accesses into the single per-scipt BindingExpression
 *
 * sequence case
 *
 * @test
 * @run
 */

var s1 = ["yo", "po"];
var s2 = ["raw", "saw"];
var sw = true;
def bs = bind if (sw) s1 else s2;
println(bs);
insert "lo" into s1;
println(bs);
sw = false;
println(bs);
insert "maw" into s2;
println(bs);
insert "so" into s1;
println(bs);
sw = true;
println(bs);
insert "caw" into s2;
println(bs);
