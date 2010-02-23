/**
 * JFXC-3693 : visibleNode throws StackOverFlow. 
 *
 * @subtest
 */

import java.lang.System;
/**
 * Base class for compiler benchmark tests.
 * Extend this class and override the test() function with benchmark test.
 * This supplies debug output methods,iteration and timer functionality.
 */

public class cbm {
   public var DEBUG=false;
   public var USETIMER=false;
   public var PAUSE=false;

   /**
    * print and println controlled by DEBUG variable, -debug option
    */
   public function debugOutln(msg:String) { if(DEBUG) println(msg);}
   public function debugOut(msg:String) { if(DEBUG) print(msg);}


   /**
    * timer function used via -time option to print average time to stdout
    */
   	function runTest( iters:Integer, f:function():Number )  {
       debugOutln("running {iters} iteration(s).");
       var timesum:Number = 0;
       for ( I in [1..iters]) {
       	var startTime = System.nanoTime();
       	var t = f();
           timesum += (System.nanoTime()-startTime)*0.000001
       }
   	println("average time: {timesum/iters} ms  iterations: {iters}");
   }

   /** simple usage message */
   function usage() {
       println("javafx mathcalcb [-debug] [-time] [-iter N] [-help|-h]");
       println("-debug  - print extra debug info, if any is used.");
       println("-time   - print time info after test completes");
       println("-pause  - hang the app, to aid in collecting whatever and kill");
       println(" -iter N - Run test N times.");
       System.exit(0);
   }

   /**
    * Calls test() N times (set by "-iter N" option)
    * No timer output. Use -time to call runTest() to print timer output.
    */
   function testi(iters:Integer){ for (i in [1..iters]) test();}

   /**
    * override this test() method with actual test method
    * This method can be call numerous time by testi() via the "-iter N" option
    */
   public function test():Number{ return 0;}

}

var bm = new cbm();
public function runtest( args:String[], bm:cbm)
{
  var iterations = 1;

  for(arg in args) {
    if(arg.compareTo("-help")==0) bm.usage();
    if(arg.compareTo("-debug")==0) {bm.DEBUG=true; println("expect slower times with extra debug activity.");}
    if(arg.compareTo("-time")==0) bm.USETIMER=true;
    if(arg.compareTo("-iter")==0) iterations = java.lang.Integer.parseInt( args[indexof arg + 1] );
    if(arg.compareTo("-pause")==0) bm.PAUSE=true;
  }

  if(bm.USETIMER) {bm.runTest(iterations, bm.test);}
  else {  bm.testi(iterations); }
  if(bm.PAUSE) java.lang.System.in.read(); // the grim reaper will take care of this.
}


