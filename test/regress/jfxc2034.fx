/**
 * Regression test JFXC-2034 : Assortis demo broken trying to call doubleValue() on a "long" var
 *
 * @test
 * @run
 */



var num = java.lang.Long.parseLong("1234");
println(num.doubleValue());
