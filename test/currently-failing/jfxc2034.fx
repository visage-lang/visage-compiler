/**
 * Regression test JFXC-2034 : Assortis demo broken trying to call doubleValue() on a "long" var
 *
 * @test/fail
 *
 * was:
 * (at)test
 * (at)run
 */

import java.lang.Long;

var num = Long.parseLong("1234");
println(num.doubleValue());
