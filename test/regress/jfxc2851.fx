/**
 * Regression test JFXC-2851 : Mixins: samples/SpringAnimation gets an NPE at com.sun.scenario.scenegraph.fx.FXNode.setTransformMatrix(FXNode.java:388)
 *
 * @test
 * @compilefirst jfxc2851a.fx
 * @compilefirst jfxc2851b.fx
 * @run
 */

class sub extends jfxc2851b { 
} 

var jj: sub = sub{}; 
function run() { 
  var xx: String = jj.getit(); 
  if (xx == null) { 
      println("fails: {xx}"); 
  } else { 
      println("passes: {xx}"); 
  } 
} 
