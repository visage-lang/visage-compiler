/**
 * JFXC-2825 : Collapse all the on-replace closures to one generated class per script
 *
 * @test
 * @run
 */

class jfxc2825 {
  var x = 0 on replace { println("yo") }
  var y = "hi" on replace old { println("{old} => {y}") }
  function f() {
    var local = "here";
    var zzz = 8 on replace { println(local) }
    zzz = 9;
  }
}

var stat = "polka" on replace { println(stat) }

var t = new jfxc2825;
t.x = 2;
t.y = "orgo";
t.f();
stat = "rumba";
