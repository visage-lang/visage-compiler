/* Rgression test JFXC-1249 : Provide isInitialized() method for variables
 *
 * @test
 * @run
 */

class ii {
  var x : Integer on replace { println( "x on replace: {isInitialized(x)}") }
  public var s : String on replace { println( "s on replace: {isInitialized(s)}") }
  var o : Object on replace { println( "o on replace: {isInitialized(o)}") }

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
   println("s run: {javafx.lang.FX.isInitialized(aii.s)}");
   aii.o = new Object();
   println("o run: {javafx.lang.FX.isInitialized(aii.o)}");
   println("Done");
}
