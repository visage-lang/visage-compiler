/**
 * Regression test: JFXC-3589 : Compiled bind: bound sequence element
 *
 * Complex references
 *
 * @test
 * @run
 */

class A {
  var seq = ["ox", "mule", "bull"];
  var k = 0;
}

var a = A{}
def bsi = bind a.seq[a.k];
println(bsi);
a.k = 1;
println(bsi);
delete a.seq[0];
println(bsi);
a.seq = ["ram", "deer"];
println(bsi);
a.k = 9;
println(bsi);
a.k = 0;
println(bsi);
a.seq = null;
println(bsi);
insert "cow" into a.seq;
println(bsi);
a = null;
println(bsi);
a = A{}
println(bsi);
