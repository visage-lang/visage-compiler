/**
 * Regression test JFXC-107 : binding to function arguments
 *
 * @test
 * @run
 */

import java.lang.System;

public class Translate {
    public attribute x: Number;
    public attribute y: Number;
    public static function translate(x:Number, y: Number): Translate {
	return Translate {
	    x: bind x
	    y: bind y
	};
    }
    public static bound function btranslate(x:Number, y: Number): Translate {
	return Translate {
	    x: bind x
	    y: bind y
	};
    }
}
var x = 2;
var y = 3;
var t = bind Translate.translate(x, y);
var tb = bind Translate.translate(x, y);
x = 10;
y = 20;
System.out.println("x={t.x} y={t.y}");
System.out.println("x={tb.x} y={tb.y}");
