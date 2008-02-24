/*
 * Regression test for JFXC-767 : Wrong receiver found for supertype implicit this call
 *
 * @test
 * @run
 */

import java.lang.System;
import java.util.BitSet;

public class Foo {
    attribute bits : BitSet = BitSet {
            public function toString():String {
                return "Class: { getClass().getSuperclass() }";
            }
    }
}

System.out.println(Foo{}.bits);


