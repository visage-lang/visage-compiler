/**
 * Regression test: JFXC-3670 : Compiled bind: bind permeates object literal initializers
 *
 * Final local referenced from function value
 *
 * @test
 * @run
 */

class jfxc3636b {
  var v : function() : String
}

function run() {
  var val = "OK";
  var z = jfxc3636b { v: function() { val } }
  println(z.v());
}
