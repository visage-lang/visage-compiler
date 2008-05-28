/* JFXC-211:  Cannot locate attribute declared in a super class with Mulitple Inheritence
 *
 * @test
 * @run
 */

import java.lang.System;
class Base1 { 
    attribute a : Integer; 
    attribute b : Integer; 

    function foo() { a + b; } 
    function moo() { a } 
} 

class Base2 { 
    attribute c : Integer; 

    function bark() { c } 
} 

class Subclass extends Base1, Base2 { 
    attribute d : Integer; 

    function foo() { a + b + c + d } 
    function wahoo() { d } 
} 


var v = Subclass { a: 1 } 
System.out.println("a={v.a}, b={v.b}, c={v.c}, d={v.d}"); 
System.out.println("foo={v.foo()}, moo={v.moo()}, bark={v.bark()}, wahoo={v.wahoo()}"); 
