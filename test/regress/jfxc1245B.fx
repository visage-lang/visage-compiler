/**
 * Regression test for JFXC-1245 : Override keyword for functions
 * Script level override (no error/arning)
 *
 * @compilefirst jfxc1245Bsub.fx
 * @test
 */

function scriptLevel() : Integer { 99 }

class jfxc1245B extends jfxc1245Bsub {}

