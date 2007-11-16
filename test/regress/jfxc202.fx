/**
 * regression test: JFXC-202 : bind foreach
 *
 * @test
 * @run
 */

var j = 50;
var xs = bind foreach (i in [1..100] where i < j) i;
j = 15;
java.lang.System.out.println(xs);
