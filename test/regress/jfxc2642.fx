/**
 * JFXC-2642 : Precedence of and/or is wrong
 *
 * @test
 * @run
 */

println(false and true or true);
println(false and false or true);
println(true or false and false);
