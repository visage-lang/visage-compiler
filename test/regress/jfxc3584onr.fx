/**
 * Regression test: JFXC-3584 : Compiled bind: bound sequence: explicit
 *
 * Bound explicit sequences of non-nullable and sequences with on-replace
 *
 * @test
 * @run
 */

class jfxc3584onr {
  var bv = 1111;
  var sv = [1..8];
  var ev = 2222;
  var srb1 = -9;
  var srb2 = -4;
  var eb = 9999;
  def qqq = bind [bv, sv, ev, [srb1..srb2], eb]
                    on replace [beg..end] = newVal { println("replace {beg}..{end} = {sizeof newVal}") };

  function doit() {
    eb = 99999;
    srb2 = -1;
    srb1 = -5;
    srb1 = -8;
    ev = 22222;
    sv = [];
    sv = [44, 55];
    bv = 11111;
  }
}

jfxc3584onr{}.doit()
