/**
 * Regression test JFXC-909 : Scope of a variable differ from bind vs non-bind
 *
 * @test
 * @run
 */

var a = {
       var a = [1,2];
       println(a);
       a
}
println(a);
