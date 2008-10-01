/* Regression test JFXC-2045 : Add isInitialize usage to VARUSE and check it
 *
 * @test
 * @run
 */

class ii {
  var x : Integer;
  var s : String;
  var o : Object;

  init {
    println("x init: {isInitialized(x)}");
    println("s init: {isInitialized(s)}");
    println("o init: {isInitialized(o)}");
  }
}

function run() {
   var aii = ii{};
   aii.x = 88;
   println("x run: {isInitialized(aii.x)}");
   aii.s = "hi";
   println("s run: {isInitialized(aii.s)}");
   aii.o = new Object();
   println("o run: {isInitialized(aii.o)}");
   println("Done");
}
