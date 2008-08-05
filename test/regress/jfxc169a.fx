import jfxc169b;

/*
 * Not true, useless but shouldn't fail:  Should fail with error on import line (JFXC-169)
 * @test
 * @compilefirst jfxc169b.fx
 */

class jfxc169a extends jfxc169b {
    function moo() {
        foo();
    }
}
