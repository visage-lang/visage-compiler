/**
 * JFXC-3751 : Compiled bind: assignment of sequence to element nests sequence
 *
 * @test
 * @run
 */

var bse : Object[];
insert "a" into bse;
insert "a" into bse;
insert "a" into bse;
bse[1] = [1..5];
println(bse);
