/**
 * Some tests so improve compatibilty coverage apart from junit tests
 *  Need runtime args for getArguments() and getArgument()
 *
 * @test
 * @run/param arg1 arg2 arg3
 */
import javafx.lang.FX;

public class JAVAFX_LANG_TEST {

	/**public static com.sun.javafx.runtime.sequence.Sequence getArguments();  
	* As documented:
	*   For JavaFX Script applications that are started on the command
	*   line,running application. Returns null if there were no incoming
	*   arguments or if this application was not invoked from the
	*   command line.
	*
	* Does not seem fxunit test can take arguments, so this is plain fx style test
	*/
	function check(i1:Integer, i2:Integer,msg:String) {	if(i1 !=i2) 		println("FAILED {msg}: {i1} != {i2}");}
	function check(s1:String[], s2:String[],msg:String) {	if(s1 !=s2) 	println("FAILED {msg}: {s1} != {s2}");}
	function check(s1:String, s2:String,msg:String) {	if(s1 !=s2) 		println("FAILED {msg}: {s1} != {s2}");}
	function check(b1:Boolean, b2:Boolean,msg:String) { if(b1 != b2)  println("FAILED {msg}: {b1} != {b2}"); }

  var expectedArgs = ["arg1","arg2","arg3"];
	  
  public function GetArgumentsTest()
	{
	  var args = FX.getArguments();
      check(expectedArgs,args,"args[]");
	}

	/** public static java.lang.Object getArgument(java.lang.String);  */
	public function GetArgumentTest()  {
		/** Gets arguments by number string. This may be subject to change. */
		var arg; var idx = 0;
		for( a in ["0","1","2"])  {   	
    		arg = FX.getArgument(a);
    	   check(arg.toString(),expectedArgs[idx++],"one arg check");
		}
	}

	public function GetArgumentNegTest()  {
		var caughtNPE = false;
		var nullarg;

		/** get non-existant argument */
		try	{
			nullarg = FX.getArgument("asdfasdf");
		}	catch (npe:java.lang.NullPointerException)	{
			caughtNPE=true;
		}
		check(false, caughtNPE,"NPE");
		if(nullarg != null) println("FAILED getARgument(non existing)");

		/** get past end of argument list */
		try	{
			nullarg = FX.getArgument("3");
		}	catch (npe:java.lang.NullPointerException)	{
			caughtNPE=true;
		}
		check(false, caughtNPE,"arg3 of 2");
		if(nullarg != null) println("FAILED getARgument(3)");


		/** get before left end of argument list */
		try	{
			nullarg = FX.getArgument("-1");
		}	catch (npe:java.lang.NullPointerException)	{
			caughtNPE=true;
		}
		check(false, caughtNPE,"arg-1 of 2");
		if(nullarg != null) println("FAILED getARgument(-1)");
	}

}

public class TestFXargs {

   function testGetArguments() {
      var jlt = JAVAFX_LANG_TEST {}
	 jlt.GetArgumentsTest();

   }

   function testGetArgument() {   
      var jlt = JAVAFX_LANG_TEST {}
      jlt.GetArgumentTest();
   }
   function testGetArgumentNeg() {   
      var jlt = JAVAFX_LANG_TEST {}
      jlt.GetArgumentNegTest();
   }

}

function run(args:String[]) {
	var tfx = TestFXargs{}
	tfx.testGetArguments();
	tfx.testGetArgument();
    tfx.testGetArgumentNeg() 
}

