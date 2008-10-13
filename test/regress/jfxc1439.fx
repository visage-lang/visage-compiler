/**
 * regression test: JFXC-1439 : No exception when dividing Duration by zero
 *
 * @test
 * @run
 */

import java.lang.Exception;
import java.lang.ArithmeticException;

var t1: Duration = 20m;

try {
    var t2 = t1.div(0);
    println ("error: no exception thrown (t2={t2})");
} catch (e1:ArithmeticException) {
    println ("ok: ArithmeticException thrown");
} catch (e2:Exception) {
    println ("error: unexpected exception thrown: {e2}");
} 
