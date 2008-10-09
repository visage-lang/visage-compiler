/* @test
 * @run
 */

var a : String = null;
var aa = [ "yada", "yada" ];
println(aa);
insert a into aa;
println(aa);

var bb : Object[] = [ "yada", null, "yada" ];
println(bb);

var cc = [ "yada", a, "yada" ];
println(cc);
