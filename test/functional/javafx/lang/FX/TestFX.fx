/**
 * Some tests so improve compatibilty coverage apart from junit tests
 *
 * @test/fxunit
 * @run
 */

import javafx.lang.FX;
import javafx.fxunit.*;

class JAVAFX_LANG_TEST {
var data:String = "";
/** covered   public static boolean isSameObject(java.lang.Object, java.lang.Object);   */
/** covered   public static boolean isInitialized(java.lang.Object);   */
/** covered   public static java.lang.String getProperty(java.lang.String);  */
/** covered   public static int addShutdownAction(com.sun.javafx.functions.Function0);   */
/** covered   public static void deferAction(com.sun.javafx.functions.Function0);   */
/** NOT covered  public static boolean removeShutdownAction(int);   

  * @param  action of type function():Void that will be removed from the Shutdown Action Stack
  * @return a Boolean value signifing sucess or failure of removing the action
*/

/** jfxc3048 addShutdown action adds this type of function declaration numerous time and return unique handles each time */
public function faction1():Void { var message = "faction1"; println(message); }
public function faction2():Void { var message = "faction2"; println(message); }

public var action1 = function():Void { var message = "action1"; println(message); }
public var action2 = function():Void { var message = "action2"; println(message); }


/**
 * test using function variables action1, action 2
 */
public function RemoveShutdownFVActionTest()
{
var handle4 =  FX.addShutdownAction( action1);
var handle5 =  FX.addShutdownAction( action2);
var handle6 =  FX.addShutdownAction( action2);
var handle7 =  FX.addShutdownAction( action1);

/**
  * attempt to add same action should return same handle 
  */
assertEquals( handle5,handle6);
assertEquals( handle4,handle7);

 /** 
   * handle6 and handle7 should be same as handle5 and handle4, so nothign should be printed 
   * without having to remove handle5 and handle4.
   */
   var ret = FX.removeShutdownAction(handle6);
   /* check that remove was successful by checking return value */
     assertEquals(ret,true);
   ret = FX.removeShutdownAction(handle7);
   /* check that remove was successful by checking return value */
     assertEquals(ret,true);

  /**
    * In fact, these should have been removed already, so should return false 
	*/
   ret = FX.removeShutdownAction(handle4);
   /* check that remove was successful by checking return value */
     assertEquals(ret,false);
   ret = FX.removeShutdownAction(handle5);
   /* check that remove was successful by checking return value */
     assertEquals(ret,false);
}

public var action86 = function():Void { var message = "action86"; println(message); }
public function RemoveShutdownActionNegTest()   {
   var handle86 =  FX.addShutdownAction( action86);
   var ret = FX.removeShutdownAction(handle86);
   /**
     * Try to remove action added by invalide handle id 
	 */
   ret = FX.removeShutdownAction(handle86+1);
   /* check that remove was not successful by checking return value */
   assertEquals(ret,false);
}

/**
 * test using function names as variables
 */
public function RemoveShutdownFActionTest()   {
var handle10 =  FX.addShutdownAction( faction1);
var handle20 =  FX.addShutdownAction( faction2);
var handle30 =  FX.addShutdownAction( faction2);
var handle40 =  FX.addShutdownAction( faction1);

/**
  * attempt to add same action should return same handle 
  */
assertEquals( handle10,handle40);
assertEquals( handle20,handle30);

 /** 
   * handle4 and handle3 should be same as handle1 and handle2, so nothign should be printed 
   * without having to remove handle1 and handle2.
   */
  var  ret = FX.removeShutdownAction(handle30);
   /* check that remove was successful by checking return value */
     assertEquals(ret,true);
   ret = FX.removeShutdownAction(handle40);
   /* check that remove was successful by checking return value */
     assertEquals(ret,true);

  /**
    * In fact, these should have been removed already, so should return false 
	*/
   ret = FX.removeShutdownAction(handle10);
   /* check that remove was successful by checking return value */
     assertEquals(ret,false);
   ret = FX.removeShutdownAction(handle20);
   /* check that remove was successful by checking return value */
     assertEquals(ret,false);
}

/**
 * test with function variables pointing to separately defined functions (as parm to addShutDownAction) with  same signature
 */ 
public function RemoveShutdownPActionTest()
{
	var data:String[] = ["some data collected during run","some data collected during run","some data collected during run"];
   /* get handle to action added */
   var handle1 =  FX.addShutdownAction(
	     function(){ var message = "action3"; println(message); } 
   );

   var handle2 =  FX.addShutdownAction(
	     function(){ var message = "action4"; println(message); } 
   );

   var handle3 =  FX.addShutdownAction(
	     function(){ delete data; println("action5"); } 
   );

  /**
   * handles 1-3 are uniquely define functions, evne thoough they are the same in function, 
   * and should all have been added successfully, so can all be removed successfully.
   */
   /* remove action added by handle1 */
   var ret = FX.removeShutdownAction(handle1);
   /* check that remove was successful by checking return value */
     assertEquals(ret,true);
   /* also, when run this should not produce the output of the shutdownaction */

   /* remove action added by handle2 */
   ret = FX.removeShutdownAction(handle2);
   /* check that remove was successful by checking return value */
     assertEquals(ret,true);
   /* also, when run this should not produce the output of the shutdownaction */

   /* remove action added by handle3 */
   ret = FX.removeShutdownAction(handle3);
   /* check that remove was successful by checking return value */
     assertEquals(ret,true);
   /* also, when run this should not produce the output of the shutdownaction */

   /**
     * Try to remove action added by invalide handle id 
	 */
     ret = FX.removeShutdownAction(handle1+1);
   /* check that remove was not successful by checking return value */
     assertEquals(ret,false);
}
}

public class TestFX extends FXTestCase  {

	function testRemoveShutdownFVAction()  {
      var jlt = JAVAFX_LANG_TEST {}
      jlt.RemoveShutdownFVActionTest();
	}

/* jfxc3048
	function testRemoveShutdownFAction()  {
      var jlt = JAVAFX_LANG_TEST {}
      jlt.RemoveShutdownFActionTest();
	}
*/
	function testRemoveShutdownPAction()  {
      var jlt = JAVAFX_LANG_TEST {}
      jlt.RemoveShutdownPActionTest();
	}

	function testRemoveShutdownActionNeg()  {
      var jlt = JAVAFX_LANG_TEST {}
      jlt.RemoveShutdownActionNegTest();
	}
}