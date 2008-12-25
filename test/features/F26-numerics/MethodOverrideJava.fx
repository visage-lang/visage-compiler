/* Java overloaded methods and their overriding and invocation from JavaFX
 * Both primitive types and wrapper classes are tested
 *
 * @compilefirst MethodOverloadJavaClass.java
 * @test
 * @run
 */

/**
 * These constants will be used to match between types
 */
def isIntOver : Integer = 100;
def isLongOver : Integer = 200;
def isByteOver : Integer = 300;
def isShortOver : Integer = 400;
def isCharOver : Integer = 500;
def isFloatOver : Integer = 600;
def isDoubleOver : Integer = 700;
def isByteLongOver : Integer = 800;
def isLongByteOver : Integer = 900;

var returnTypeOver : Integer = 0;


/**
 * Parameters values
 */
var i : Integer = 30000;
var l : Long = 2200000000;
var b : Byte = 10;
var s : Short = 30000;
var c : Character = 10;
var f : Float = 3.1415926535;
var d : Double = 2.71828183;
var n : Number = 2.2;

/**
 * Class that overrides overloaded Java methods
 */
class Child extends MethodOverloadJavaClass {
    override function testOverloadRetInt(x : Integer) : Integer {return isIntOver;}
    override function testOverloadRetInt(x : Long) : Integer {return isLongOver;}
    override function testOverloadRetInt(x : Byte) : Integer {return isByteOver;}
    override function testOverloadRetInt(x : Short) : Integer {return isShortOver;}
    override function testOverloadRetInt(x : Character) : Integer {return isCharOver;}
    override function testOverloadRetInt(x : Float) : Integer {return isFloatOver;}
    override function testOverloadRetInt(x : Double) : Integer {return isDoubleOver;}
    override function testOverloadRetInt(x : Byte, y : Long) : Integer {return isByteLongOver;}
    override function testOverloadRetInt(x : Long, y : Byte) : Integer {return isLongByteOver;}

    override function testOverloadRetVoid(x : Integer) {returnType = isIntOver;}
    override function testOverloadRetVoid(x : Long) {returnType = isLongOver;}
    override function testOverloadRetVoid(x : Byte) {returnType = isByteOver;}
    override function testOverloadRetVoid(x : Short) {returnType = isShortOver;}
    override function testOverloadRetVoid(x : Character) {returnType = isCharOver;}
    override function testOverloadRetVoid(x : Float) {returnType = isFloatOver;}
    override function testOverloadRetVoid(x : Double) {returnType = isDoubleOver;}
    override function testOverloadRetVoid(x : Byte, y : Long) {returnType = isByteLongOver;}
    override function testOverloadRetVoid(x : Long, y : Byte) {returnType = isLongByteOver;}
}

var tester = new Child;

function run() {
    // Testing Java overloaded inherited methods overriden in JavaFX
    println("Java overloaded inherited methods overriden in JavaFX");
    testPrimitive();
}

/**
 * Testing Java overloaded methods with primitive types as argumets
 */
function testPrimitive() {
    println("Primitive");
    println(tester.testOverloadRetInt(i));
    println(tester.testOverloadRetInt(l));
    println(tester.testOverloadRetInt(b));
    println(tester.testOverloadRetInt(s));
    println(tester.testOverloadRetInt(c));
    println(tester.testOverloadRetInt(f));
    println(tester.testOverloadRetInt(d));
    println(tester.testOverloadRetInt(n));
    println(tester.testOverloadRetInt(b, l));
    println(tester.testOverloadRetInt(l, b));

    tester.testOverloadRetVoid(i);
    println(tester.returnType);

    tester.testOverloadRetVoid(l);
    println(tester.returnType);

    tester.testOverloadRetVoid(b);
    println(tester.returnType);

    tester.testOverloadRetVoid(s);
    println(tester.returnType);

    tester.testOverloadRetVoid(c);
    println(tester.returnType);

    tester.testOverloadRetVoid(f);
    println(tester.returnType);

    tester.testOverloadRetVoid(d);
    println(tester.returnType);

    tester.testOverloadRetVoid(n);
    println(tester.returnType);

    tester.testOverloadRetVoid(b, l);
    println(tester.returnType);

    tester.testOverloadRetVoid(l, b);
    println(tester.returnType);
}

