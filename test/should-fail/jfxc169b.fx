import junit.framework.*;
import java.lang.System;

/*
 * @subtest jfxc169a.fx
 */

class jfxc169b {
    function foo() {
        System.out.println("blah");
    }

    function fail(message : String) {
        System.out.println("Failed: {message}");
    }
}