/**
 * Regression test JFXC-2011 : Compilation fails for a valid bind
 *
 * @test/fail
 * Should be:
 *  (at)test
 *  (at)run
 */

var s ;
var t = bind s;
s= "JavaFX";
println(t);
