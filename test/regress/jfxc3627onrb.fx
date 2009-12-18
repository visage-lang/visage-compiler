/**
 * Regression test JFXC-3627 : Compiled bind: bound sequence: function invocation
 *
 * @test
 * @run
 */

function len(z : String[]) : Integer[] { sizeof z }
var seq = ["ha", "fa"];
var cnt = 0;
def bs = bind len(seq)  on replace [a..b] = newVal { println("{a}..{b} = { newVal}") };
insert "ba" into seq;
delete seq;
insert ["na", "xa"] into seq;
delete "xa" from seq;
