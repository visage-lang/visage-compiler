/* Java overloaded methods and their invocation from JavaFX
 * Both primitive types and wrapper classes are tested
 *
 * @compilefirst MethodOverloadJavaClass.java
 * @test
 * @run
 */

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
 * Class that will inherit all Java overloaded methods
 */
class Child extends MethodOverloadJavaClass {}


var tester = new MethodOverloadJavaClass;

function run() {
    // Testing calls to Java overloaded methods
    println("Java overloaded methods");
    testPrimitive();
    testWrapper();

    // Testing overloaded inherited methods from Java class
    println("Java overloaded inherited methods");
    tester = new Child;
    testPrimitive();
    testWrapper();
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

/**
 * Testing Java overloaded methods with wrapper classes as argumets
 */
function testWrapper() {
    println("Wrapper");
    println(tester.testOverloadWrapperRetInt(i));
    println(tester.testOverloadWrapperRetInt(l));
    println(tester.testOverloadWrapperRetInt(b));
    println(tester.testOverloadWrapperRetInt(s));
    println(tester.testOverloadWrapperRetInt(c));
    println(tester.testOverloadWrapperRetInt(f));
    println(tester.testOverloadWrapperRetInt(d));
    println(tester.testOverloadWrapperRetInt(n));
    println(tester.testOverloadWrapperRetInt(b, l));
    println(tester.testOverloadWrapperRetInt(l, b));

    tester.testOverloadWrapperRetVoid(i);
    println(tester.returnType);

    tester.testOverloadWrapperRetVoid(l);
    println(tester.returnType);

    tester.testOverloadWrapperRetVoid(b);
    println(tester.returnType);

    tester.testOverloadWrapperRetVoid(s);
    println(tester.returnType);

    tester.testOverloadWrapperRetVoid(c);
    println(tester.returnType);

    tester.testOverloadWrapperRetVoid(f);
    println(tester.returnType);

    tester.testOverloadWrapperRetVoid(d);
    println(tester.returnType);

    tester.testOverloadWrapperRetVoid(n);
    println(tester.returnType);

    tester.testOverloadWrapperRetVoid(b, l);
    println(tester.returnType);

    tester.testOverloadWrapperRetVoid(l, b);
    println(tester.returnType);
}

