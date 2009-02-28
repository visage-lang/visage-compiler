/**
 * JFXC-2813 : For-loop over range generates unnecessary garbage objects on heap
 *
 * Literal range elements
 *
 * @test
 * @run
 */

for (xxx in [1..9])
  println(xxx);
println('------');
for (xxx in [1..9 step 2])
  println(xxx);
println('------');
for (xxx in [9..1 step -1])
  println(xxx);
println('------');
for (xxx in [9..1 step -5])
  println(xxx);
println('------');
for (xxx in [1..<9])
  println(xxx);
println('------');
for (xxx in [1..<9 step 2])
  println(xxx);
println('------');
for (xxx in [9..<1 step -1])
  println(xxx);
println('------');
for (xxx in [9..<1 step -2])
  println(xxx);
println('------');
