/*
 * Method overloading
 *
 * @test/fxunit
 * @run
 */
import javafx.fxunit.*;
import javafx.lang.FX;

public class NumOverload1 extends FXTestCase {

function overloadFoo(i : Integer):String {FX.println("Integer: {i}"); return "Integer";}
function overloadFoo(i : Long):String {FX.println("Long: {i}"); return "Long";}
function overloadFoo(i : Byte):String {FX.println("Byte: {i}"); return "Byte";}
function overloadFoo(i : Character):String {FX.println("Character: {i}"); return "Character";}
function overloadFoo(i : Short):String {FX.println("Short: {i}"); return "Short";}
//function overloadFoo(i : Float):String {FX.println("Float: {i}"); return "Float";} // overloadFoo(float) is already defined
function overloadFoo(i : Double):String {FX.println("Double: {i}"); return "Double";}
//function overloadFoo(i : Number):String {FX.println("Number: {i}"); return "Number";} // overloadFoo(float) is already defined
function overloadFoo(i : String):String {FX.println("String: {i}"); return "String";}
function overloadFoo(i : Duration):String {FX.println("Duration: {i}"); return "Duration";}
    public function testInteger() {
        var i : Integer = 987654321;
        var r = overloadFoo(i);
        assertEquals("Integer", r);
    }
    function testLong() {
        var i : Long = 123456789;
        var r = overloadFoo(i);
        assertEquals("Long", r);
    }
    function testByte() {
//        var i : Byte = 123456789; //Byte out of range
        var i : Byte = 55;
        var r = overloadFoo(i);
        assertEquals("Byte", r);
    }
    function testCharacter() {
        var i : Character = 123456789; //warning: possible loss of precision
        var r = overloadFoo(i);
        assertEquals("Character", r);
    }
    function testShort() {
//        var i : Short = 123456789;  //Short out of range
        var i : Short = 5555;
        var r = overloadFoo(i);
        assertEquals("Short", r);
    }
    function testFloat() {
        var i : Float = 123456789;
        var r = overloadFoo(i);
// JFXC-2408#87
//        assertEquals("Float", r);   
    }
    function testDouble() {
        var i : Double = 123456789;
        var r = overloadFoo(i);
        assertEquals("Double", r);
    }
    function testNumber() {
        var i : Number = 123456789;
        var r = overloadFoo(i);
// JFXC-2408#87
//        assertEquals("Number", r);
    }
    function testString() {
//        var i : String = 123456789; // incompatible types
        var i : String = "123456789";
        var r = overloadFoo(i);
        assertEquals("String", r);
    }
    function testDuration() {
//        var i : Duration = 123456789; //incompatible types
        var i : Duration = 123456789ms;
        var r = overloadFoo(i);
        assertEquals("Duration", r);
    }

}
