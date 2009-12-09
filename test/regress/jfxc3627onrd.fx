/**
 * Regression test JFXC-3627 : Compiled bind: bound sequence: function invocation
 *
 * @test
 * @run
 */

var seq = ["hi"];
def bseq = bind seq;
def bbseq = bind bseq  on replace [a..b] = newVal { println("{a}..{b} = {sizeof newVal}") };
insert "ri" into seq;
insert "li" into seq;
seq = ["fin"];
seq = [];
