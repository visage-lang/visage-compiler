/**
 * JFXC-2560 : Attribution does not propagate expected type in bound explicit sequence numeric conversion
 *
 * @test/fail
 */

function run() {
   var d : Double[] = bind 43.0;

   println(d);
}
