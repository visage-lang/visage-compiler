/**
 * JFXC-4121 : DisplacementMapTest throws java.lang.IllegalArgumentException: Map data must be non-null
 *
 * Emulation
 *
 * @test
 * @run
 */

public class jfxc4121 {
   var floatMap : String;

   function show(s : String) { 
      println("Got: {s}");
      s
   }

   init {
      floatMap = "All mapped up";
      var canvas = bind show(floatMap) on replace { println("on-replace {canvas}"); }
   }
}

function run() {
   jfxc4121{}
}
