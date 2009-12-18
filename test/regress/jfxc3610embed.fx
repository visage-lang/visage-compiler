/**
 * Regression test: JFXC-3610 : Compiled-bind: bugs in handling embedded bound sequences
 *
 * Handling of bound implicit sequence conversion and bound [] 
 *
 * @test
 * @run
 */

var isness = bind [2, 4, 6] == [2..6 step 2];
println(isness);
