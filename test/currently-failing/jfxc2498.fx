/**
 * JFXC-2498 : Duration arithmetic bugs
 *
 * @test/fail
 */

var d1 : Duration = 200ms;
var d2 : Duration = 50ms;
def avg : Duration = bind (d1 + d2) / 2;
println( avg );
d1 = 1s;
println( avg );
d2 = 2s;
println( avg );
