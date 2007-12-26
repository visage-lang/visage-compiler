/*
 * Regression test: Infer delete trigger oldValue type from context.
 * Failing as part of JFXC-472; assigned to brian
 *
 * @test
 * @run/fail
 */

import java.lang.System;

class Base {
    attribute foo = 1 on replace (old) {
        System.out.println("Base.foo={foo}, old={old}");
    }
    
    init {
        System.out.println("Base.init");
        foo = 2;
    }
}

var a = new Base; 
