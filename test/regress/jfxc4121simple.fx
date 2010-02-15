/**
 * JFXC-4121 : DisplacementMapTest throws java.lang.IllegalArgumentException: Map data must be non-null
 *
 * Simple case
 *
 * @test
 * @run
 */

class jfxc4121simple {
  var obj : Object = 4;

  init {
    if (obj instanceof String) {
      def so = obj as String;
      println("huh?")
    } else {
      println("OK")
    }
  }
}

jfxc4121simple{}
