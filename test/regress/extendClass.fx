import extendClassHelper;

/*
 * TEST DISABLED: put @ signs back on next two lines to enable!
 * test
 * compile extendClassHelper.fx
 */

class extendClass extends extendClassHelper {
    function moo() {
        foo();
    }
}
