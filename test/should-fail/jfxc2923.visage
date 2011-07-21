/**
 * Regress test for JFXC-2923.
 *
 *  Referencing a non-static instance var from a static 
 *  context causes a javadump, and an incorrect error msg.
 *
 * @test/compile-error
 */

class jfxc2923 {
   var x = 10;
}

// this line used to crash in back-end
jfxc2923.x = 90;

