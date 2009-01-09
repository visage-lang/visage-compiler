/*
 * Incorrect dispatching overloaded methods with Byte/Double/Float parameters
 *
 * After fixing please uncomment and update corresponding testcases in
 *     test/features/F26-numerics/NumOverload1.fx
 *
 * @test/fxunit
 * @
 */
import javafx.fxunit.*;
import javafx.lang.FX;

public class jfxc2605 extends FXTestCase {

function overload(x: Byte):String {" => called overload(x: Byte)";}
//function overload(x: Double):String {" => called overload(x: Float)";}   // uncomment after fixing
function overload(x: Double):String {" => called overload(x: Double)";}
//function overload(x: Double):String {" => called overload(x: Number)";}  // uncomment after fixing
   
    function testDouble() {
        FX.println("testDouble: calling overload(Double)");
        var v : Double = 123456789;
        var expected: String = " => called overload(x: Double)";
        var actual: String = overload(v);
        assertEquals(expected, actual);  
    }
    function testFloat() {
        FX.println("testFloat: calling overload(Float)");
        var v : Float = 123456789;
        var expected: String = " => called overload(x: Float)";
        var actual: String = overload(v);
        // assertEquals(expected, actual);    // uncomment after fixing
    }
    function testNumber() {
        FX.println("testNumber: calling overload(Number)");
        var v : Number = 123456789;
        var expected: String = " => called overload(x: Number)";
        var actual: String = overload(v);
        // assertEquals(expected, actual);  // uncomment after fixing
    }

} 
