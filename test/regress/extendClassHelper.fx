import junit.framework.*;
import java.lang.System;

class extendClassHelper {
    function foo() {
        System.out.println("blah");
    }

    function fail(message : String) {
        Assert.fail(message);
    }
}