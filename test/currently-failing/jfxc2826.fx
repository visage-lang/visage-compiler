/**
 * currently-failing test for JFXC-2826 : internal error in the OpenJFX compiler (1.1.0) when using "on replace" with sequence
 *
 * @test/fail
 */

var t =[1, 2] on replace jfxc2826 [a..b]=newElements {
    println(t);
};
