/**
 * JFXC-2737 : Latest Marina compiler workspace fails to compile Marina Node.fx file
 *
 * @test
 * @run
 */

class jfxc2737 {
   var a = 1;
   var b = 2;
   var c = 4;
   var x = bind lazy foo(a, b, c);

   function foo(a : Integer, b : Integer, c : Integer) {
      a + b + c;
   }
}

println(jfxc2737{}.x);
