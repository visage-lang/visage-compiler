/*
 * @test
 * @run
 */
 
import java.lang.System;

class jfxc1058 {
    public attribute seq: Integer[] on replace oldSeq[a..b] = newSlice {
        java.lang.System.out.println("replace {oldSeq[a..b].toString()} with {newSlice.toString()}");
    }
}

jfxc1058 {
    seq: bind [1, 2, 3];
}
