import extendClassHelper;

/*
 * SHOULD NOT FAIL -- compiler bug
 * @test/fail
 * @compile extendClassHelper.fx
 */

class extendClass extends extendClassHelper {
    function moo() {
        foo();
    }
}
