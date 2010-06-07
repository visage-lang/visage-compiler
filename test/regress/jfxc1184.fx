/*
 * Regression: JFXC-1184 - Compiler throws internal error while trying to access vararg method of superclass.
 *
 * @compilefirst jfxc1184J.java
 * @test
 * @run
 *
 */

class Test extends jfxc1184J {
}
var t = Test{};
t.method();
