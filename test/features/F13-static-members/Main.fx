/* Feature test #13 - static members
 * Demonstrates: static functions and attributes
 * @test
 * @run
 */

import java.lang.System;

class Test {
    static attribute a : Integer = 14;
    attribute b : Integer = 55;
    static function doublea() { a + a }
}

var ah = new Test;

System.out.println("static: {ah.a},  {ah.b}");

Test.a = 3;
System.out.println("static: {ah.a}, twice: {ah.doublea()}, twice: {Test.doublea()}");

ah.b = 99;
System.out.println("static: {Test.a}, {ah.b}");

var tr = 77;

ah = Test {
    b: 71717
};

System.out.println("static: {Test.a},  {ah.b}");

