/**
 * JFXC-3587 : Compiled bind: bound sequence: reverse
 *
 * Check shreding of non-identifers
 *
 * @test
 * @run
 */

function upto(x : Integer) {
  [1..x]
}

class Nd {
  var bb = [false, false, true]
}

var nda = Nd{};
def rnda = bind reverse nda.bb;
println(rnda);
insert true into nda.bb;
println(rnda);

var n = 5;
def bf = bind reverse upto(n);
println(bf);
n = 3;
println(bf);


