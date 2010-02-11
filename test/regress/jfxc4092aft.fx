/**
 * JFXC-4092 : sequence bound-if with logical-and condition gives stale explicit sequence element
 *
 * Elements evaluated minimal number of times -- mixed elements -- trigger access
 *
 * @test
 * @run
 */

class jfxc4092aft {
  var x = 4 on replace { if (seq != [x, [0..x/5], x]) println("MISMATCH: seq: {seq}") }
  var sx = [2];
  function foo(z : Integer) { println("Foo z: {z}"); z}
  var seq = bind [foo(x), [0..x/5], foo(x)];
  var aft = bind x on replace { if (seq != [x, [0..x/5], x]) println("MISMATCH after: seq: {seq}") }
}
var aa = jfxc4092aft{};
println(aa.seq);
aa.x = 13;
aa.x = 29;
println(aa.seq);
println(aa.seq);
println(aa.seq);
println(aa.seq);
