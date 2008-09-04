/* Rgression test JFXC-1249 : Provide isInitialized() method for variables
 *
 * @test
 * @run/fail
 */

class ii {
  var x : Integer;

  init {
    println("isInitialized (tested in init): {isInitialized(x)}");
  }
}

function run() {
   var aii = ii{};
   aii.x = 88;
   println("isInitialized (tested in run): {isInitialized(aii.x)}");
   println("Done");
}
