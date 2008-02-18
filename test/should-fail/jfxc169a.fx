import jfxc169b;

/*
 * SHOULD NOT FAIL -- compiler bug (JFXC-169)
 * @test
 * @compile jfxc169b.fx
 */

class jfxc169a extends jfxc169b {
    function moo() {
        foo();
    }
}
