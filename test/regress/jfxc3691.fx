/**
 * Regression test for JFXC-3691 : Compiled bind: indexof in nested for gets an NPE
 *
 * @test
 */

var outer = [0..3];
var inner = [10..13];

for (xx in outer) {
   for (yy in inner) {
        indexof yy;
   }
}
