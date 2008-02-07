/*
 * Regression test: show that numeric conversion (Integer to Number) works
 *
 * @test
 * @run
 */

import java.lang.Math;
import java.lang.System;

class Foo {
    
    static function max(n:Number, m:Number):Number { Math.max(n, m); }
    function baz(): Number {
        var y = bind max(1.0, 1);
	y
    }
}

System.out.println((Foo{}).baz());
