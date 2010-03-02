/**
 * JFXC-4121 : DisplacementMapTest throws java.lang.IllegalArgumentException: Map data must be non-null
 *
 * Core of the issue
 *
 * @test
 * @run
 */

class jfxc4121core {
  var num = 4;

  init {
    num = 12345;
    var furd = bind num;
    var mine = num;
    println(mine);
  }
}

jfxc4121core{}
