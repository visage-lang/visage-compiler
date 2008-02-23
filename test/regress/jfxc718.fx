/*
 * Regression test for JFXC-757 : NPE on non-constant override attribute default
 *
 * @test
 * @run
 */

import java.lang.System;

class A extends java.util.BitSet {
  attribute a : Integer
    on replace { System.out.println("trigger"); }
}

class Test extends A {
}

Test { }
