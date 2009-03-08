/**
 * currently-failing test for JFXC-2884 : Compiler crash: type inference on first assign breaks on bind
 *
 * @test/fail
 */

var x;
var y = bind x ;//with inverse;

x = 5;
println(y);
