import jfxc169b;

/*
 * Should fail with error on import line (JFXC-169)
 * @test/fail
 * @compile jfxc169b.fx
 */

class jfxc169a extends jfxc169b {
    function moo() {
        foo();
    }
}
