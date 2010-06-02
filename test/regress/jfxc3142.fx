/*
 * Regression: JFXC-3142 - Trigger fires even though values haven't changed.
 *
 * @test
 * @run
 *
 */

class ImmutableA {
    public-init var a:Number;
    public-init var b:Number;
}

var n:Number = 0;
var ia = bind ImmutableA { a: 1, b: n };
var x:Number = bind ia.a on replace old {
    println("Old={old}, New={x}");
}

n++;
n++;
n++;
n++;
