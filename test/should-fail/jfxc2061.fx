/*
 * Regression test: JFXC-2061: Crash in back-end attribution declaring a variable which is already declared in the outer block.
 *
 * @test/compile-error
 */

for (i in [1..10]){
  while (i > 5 ) {
   var i:Integer=1;
  }
}
