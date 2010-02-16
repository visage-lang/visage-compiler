/**
 * regression test: JFXC-3947 : k instanceof com.sun.javafx.runtime.sequence.Sequence returns true even when it is not
 *
 * @test
 * @run
 */

def k:Object = "Some String";
def isSeq = (k instanceof com.sun.javafx.runtime.sequence.Sequence);
println(isSeq);
