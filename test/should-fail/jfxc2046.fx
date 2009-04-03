/*
 * Regression test: JFXC-2046: Compiler does not report error for a anonymous Void (return type ) function with only one return statement
 *
 * @test/compile-error
 */

var s:function():Void = function():Void { return 2; };
