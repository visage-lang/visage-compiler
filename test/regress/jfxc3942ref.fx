/**
 * JFXC-3942 : missing updates on bind to external script-level var
 *
 * From varioius contexts
 *
 * @compilefirst jfxc3942refSub.fx
 * @test
 * @run
 */

var z = 9;
public class A {
   def ax = bind jfxc3942refSub.val;
   def az = bind z;
   init {
     println("+A");
     println("ax: {ax}");
     jfxc3942refSub.val = 66;
     println("ax: {ax}");
     println("az: {az}");
     z = 33;
     println("az: {az}");
     println("-A");
   }
}
def x = bind jfxc3942refSub.val;
def bz = bind z;

class jfxc3942ref {
   var ii = 0;
   def vx = bind jfxc3942refSub.val;
   def vii = bind ii;
   def vz = bind z;
   init {
     println("+v1");
     println("vx: {vx}");
     jfxc3942refSub.val = 55;
     println("vx: {vx}");
     println("vii: {vii}");
     ii = 77;
     println("vii: {vii}");
     println("vz: {vz}");
     z = 11;
     println("vz: {vz}");
     println("-v1");
   }
}

function run() {
  jfxc3942ref{};
  println(A{}.ax);
  println(x);
  jfxc3942refSub.val = 9999;
  println(x);
}