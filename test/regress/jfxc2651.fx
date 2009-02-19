/*
 * Regression test for JFXC-2651 : fix for JFXC-833 crashes compiler on test of incomparable types
 *
 * @test
 * @run
 */

import java.util.*;
import java.lang.System;

//reference types

class A {}
mixin class B {}
class C extends A,B {}
var c = C{};
var b:B = c;
var a:A = c;
var x:ArrayList = new ArrayList();

System.out.println(a == (x as ArrayList));
System.out.println([a] == (x as ArrayList));
System.out.println(a == [(x as ArrayList)]);
System.out.println([a] == [(x as ArrayList)]);

System.out.println((x as ArrayList) == a);
System.out.println([(x as ArrayList)] == a);
System.out.println((x as ArrayList) == [a]);
System.out.println([(x as ArrayList)] == [a]);

System.out.println(a == (x as List));
System.out.println([a] == (x as List));
System.out.println(a == [(x as List)]);
System.out.println([a] == [(x as List)]);

System.out.println((x as List) == a);
System.out.println([(x as List)] == a);
System.out.println((x as List) == [a]);
System.out.println([(x as List)] == [a]);

System.out.println(a == b);
System.out.println(b == a);

System.out.println([a] == b);
System.out.println(b == [a]);

System.out.println([a] == [b]);
System.out.println([b] == [a]);

System.out.println((x as ArrayList) == (x as Collection));
System.out.println([(x as ArrayList)] == (x as Collection));
System.out.println((x as ArrayList) == [(x as Collection)]);
System.out.println([(x as ArrayList)] == [(x as Collection)]);

//null

System.out.println([1] == null);
System.out.println(null == [1]);

System.out.println([a] == null);
System.out.println(null == [a]);

System.out.println(a == null);
System.out.println(null == a);

System.out.println(null == null);

System.out.println(a == []);

//primitives

System.out.println(1 == 1.0);
System.out.println(1.0 == 1);

System.out.println([1] == 1.0);
System.out.println(1.0 == [1]);

System.out.println([1.0] == 1);
System.out.println(1 == [1.0]);

System.out.println([1.0] == [1]);
System.out.println([1] == [1.0]);
