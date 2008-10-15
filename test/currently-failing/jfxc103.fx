/*
 * jfxc103: Naming conflict between Boolean/Number and java.lang.Boolean/Number
 *
 * Not sure the bug description is accurate anymore, but it fails to compile.
 * @test
 *
 * If there is to be a jfxc103.fx.EXPECTED it should probabably contain
 true
 true
 */

var b = true;
println(b instanceof Boolean);

var i = 1;
println( i instanceof Integer);


