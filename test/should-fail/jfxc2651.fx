/*
 * Should fail test for JFXC-2651 : fix for JFXC-833 crashes compiler on test of incomparable types
 *
 * @test/compile-error
 */

import java.util.*;
import java.lang.System;

//references

class A {}
var a:A = null;
var x:Object = null;

(a == (x as java.lang.String));
((x as java.lang.String) == a);

([a] == (x as java.lang.String));
((x as java.lang.String) == [a]);

(a == [(x as java.lang.String)]);
([(x as java.lang.String)] == a);

([a] == [(x as java.lang.String)]);
([(x as java.lang.String)] == [a]);

((x as ArrayList) == (x as HashSet));
((x as ArrayList) == [(x as HashSet)]);
([(x as ArrayList)] == (x as HashSet));
([(x as ArrayList)] == [(x as HashSet)]);

//primitives

System.out.println(1 == false);

System.out.println([1] == false);
System.out.println(false == [1]);

System.out.println([false] == 1);
System.out.println(1 == [false]);

System.out.println([false] == [1]);
System.out.println([1] == [false]);
