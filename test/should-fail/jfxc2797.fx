/**
 * Should-fail test for JFXC-2797 : instanceof issue
 *
 * @test/compile-error
 */

var str1 = "hello";
println(str1 instanceof String);
println(str1 instanceof java.lang.Integer);
