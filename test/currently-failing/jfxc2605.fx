/*
 * Incorrect dispatching overloaded methods with Byte/Double/Float parameters
 *
 * After fixing please uncomment and update corresponding testcases in
 *     test/features/F26-numerics/NumOverload1.fx
 *
 * @test
 * @run/fail
 */

import javafx.lang.FX;

function overload(x: Byte):String {" => called overload(x: Byte)";}
//function overload(x: Double):String {" => called overload(x: Float)";}
function overload(x: Double):String {" => called overload(x: Double)";}
//function overload(x: Double):String {" => called overload(x: Number)";}

var expected:String = "";
var actual:String = "";

FX.println("testDouble: calling overload(Double)");
var d : Double = 123456789;
expected = " => called overload(x: Double)";
actual   = overload(d); 
if (expected != actual)
    throw new java.lang.Exception("Expected: {expected}\nActual; {actual}");

FX.println("testFloat: calling overload(Float)");
var f : Float = 123456789;
expected = " => called overload(x: Float)";
actual   = overload(f); 
if (expected != actual)
    throw new java.lang.Exception("Expected: {expected}\nActual; {actual}");

FX.println("testNumber: calling overload(Number)");
var n : Number = 123456789;
expected = " => called overload(x: Number)";
actual   = overload(n); 
if (expected != actual)
    throw new java.lang.Exception("Expected: {expected}\nActual; {actual}");
