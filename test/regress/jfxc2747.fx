/**
 * JFXC-2747 : non-bind translation cannot always convert from non-sequence to sequence
 *
 * @test
 * @run
 */

var a:String[] = "yo";
var b:String[] = if (true) "foo" else null;
var c:String[] = if (true) "foo" else "boo";
var d:String[] = if (false) "foo" else null;
var e:String[] = if (false) "foo" else "boo";

println(a);
println(b);
println(c);
println(d);
println(e);
