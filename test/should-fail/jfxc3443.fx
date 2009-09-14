/**
 * Regress test for JFXC-3443: attribute invalidate triggers
 *
 * @test/compile-error
 */

var y = 1;
var x = y on invalidate { println("inv"); } //error x is not bound
var z = bind for (i in [0..5]) {
   var u = bind (i mod 2 == 0) on invalidate { //error - trigger in bind context
       println("u changed");
   }
}
