/**
 * JFXC-4158 : Laziness of bind incompatible with fwd reference?
 *
 * @test
 * @run
 * 
 */

var a = bind y - 30;
var x = 0;
var trigger = 10 on replace { x = x + a };
var y:Integer = bind x on invalidate { println("invalidate"); }
println("initial a is {a}");
trigger = 20;
println("final a is {a}");
