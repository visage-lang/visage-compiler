/**
 * JFXC-2779 : crash: nested for-loops with same index name that use indexof generate conflicting indexof$x variable
 *
 * @test/fail
 */

for (x in [0..5]) {
  var y = indexof x;
  for (x in [0..5]) {
    indexof x
  }
}
