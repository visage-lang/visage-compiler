/**
 * Regression test for JFXC-1811 : Cannot access package members/classes in Java file
 *
 * Cannot currently compile Java code, see src/regress_java_src
 * *compilefirst jfxc1811sub.java
 * @test
 * @run
 */

import java.lang.System;

var k = new jfxc1811sub;
System.out.println(k.rats);
System.out.println(k.foo());
