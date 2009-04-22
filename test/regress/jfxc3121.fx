/**
 * Regression test for JFXC-3121 : public bound function of a mixin is not visible.
 *
 * @compilefirst jfxc3121Mixin.fx
 * @test
 */

public class jfxc3121 {

    public function simple() {
        var c:jfxc3121Mixin = jfxc3121Mixin.get();
        c.worksOk();
        c.failsToCompile();
    }
}
