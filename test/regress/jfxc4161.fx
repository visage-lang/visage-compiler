/*
 * Regression: JFXC-4161 - Compiled bind optimization: new style functions need to be usable from Java
 *
 @ @compilefirst jfxc4161J.java
 * @test
 * @run
 *
 */

var a = jfxc4161J.getF();
var b = a(10, 20);
println("{b}");
