/*
 * Test single assignments for variables declared at different levels
 * (script, instance and local).
 *
 * @test/fxunit
 * @run
 */

import javafx.fxunit.FXTestCase;

    // The below line causes a compiler error:
    // [java] test\features\F26-numerics\variables\VarTestScopes.fx:20: incompatible types
    // [java] found   : String
    // [java] required: char
    // [java]     def c1 : Character = 'X';
    // [java]                          ^
    // (moving to curently-failing/{TBD})
// var c1 : Character = 'X';
var b1 : Byte = 10;
var l1 : Long = 2000000000;
var s1 : Short = 30000;
var f1 : Float = 3.1415926535;
var d1 : Double = 2.71828183;

public class VarScopes extends FXTestCase {
//    def c2 : Character = 'X';
    def b2 : Byte = 10;
    def l2 : Long = 2000000000;
    def s2 : Short = 30000;
    def f2 : Float = 3.1415926535;
    def d2 : Double = 2.71828183;

    function testLocalVariables() {
//        var c3 : Character = 'X';
        var b3 : Byte = 10;
        var l3 : Long = 2000000000;
        var s3 : Short = 30000;
        var f3 : Float = 3.1415926535;
        var d3 : Double = 2.71828183;
        
//        assertEquals(c3, 'X' as Character);
        assertEquals(b3, 10 as Byte);
        assertEquals(l3, 2000000000 as Long);
        assertEquals(s3, 30000 as Short);
//        assertEquals(f3, 3.1415926535 as Float);
//        assertEquals(d3, 2.71828183 as Double);
    }

    function testInstanceVariables() {
//        assertEquals(c2, 'X' as Character);
        assertEquals(b2, 10 as Byte);
        assertEquals(l2, 2000000000 as Long);
        assertEquals(s2, 30000 as Short);
//        assertEquals(f2, 3.1415926535 as Float);
//        assertEquals(d2, 2.71828183 as Double);
    }

    function testScriptVariables() {
//        assertEquals(c1, 'X' as Character);
        assertEquals(b1, 10 as Byte);
        assertEquals(l1, 2000000000 as Long);
        assertEquals(s1, 30000 as Short);
//        assertEquals(f1, 3.1415926535 as Float);
//        assertEquals(d1, 2.71828183 as Double);
    }
}


