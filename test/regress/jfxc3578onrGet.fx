/**
 * Regression test: JFXC-3578 : Compiled bind: translation of non-transformative bound sequences: identifier and member select
 *
 * Bound member select sequence of Object WITH on-replace intermixed with gets of value
 *
 * @test
 * @run
 */

class bs3 {
  var q : Object[] = ["nope"]
}

var sel = bs3 {q: [0..5] };
def bq = bind sel.q on replace [beg..end] = newVal { println("  Replace [{beg}..{end}] = {sizeof newVal}") };
println(bq);
insert "first" into sel.q;
println(bq);
delete 3 from sel.q;
println(bq);
sel = bs3 {q: "second"};
println(bq);
sel = bs3 {q: [10..100 step 10]};
println(bq);
insert "fourth" into sel.q;
println(bq);
