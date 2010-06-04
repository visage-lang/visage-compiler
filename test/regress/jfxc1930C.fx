/*
 * Regression: JFXC-1930 - second-level inheritance of java interfaces and override.
 *
 * @compilefirst jfxc1930T.java
 * @test
 *
 */

class DoableC extends jfxc1930T {
    override function getId(): Integer { return 1 }
    function run() { java.lang.System.out.println("run") }
}
