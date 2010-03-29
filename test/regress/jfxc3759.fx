/**
 * JFXC-3759 : Compiled bind: Cannot find symbol: method size$seq(), seq is a Sequence defined at the top level in a separate file - the qualifier is missing
 *
 * @compilefirst jfxc3759sub.fx
 * @test
 */

class seq {
    var field1: Integer;
}
function run() {
    seq {
       field1: bind jfxc3759sub.seq[1];
    }
}