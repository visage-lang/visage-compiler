/**
 * Regression test JFXC-3676 : Compiled bind: applyDefaults and VarInit
 *
 * @compilearg -XDfwdRefError=false
 * @test
 * @run
 */

class jfxc3676forward {
  var x = y + 1000;
  var y = 7;
  var z = bind y;
}

println(jfxc3676forward{}.x);
