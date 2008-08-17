import junit.framework.*;
import java.lang.System;

/*
 * @subtest jfxc169a.fx
 */

public class jfxc169b {
    package function foo() {
        System.out.println("blah");
    }

    function fail(message : String) {
        System.out.println("Failed: {message}");
    }
}
