/*
 * @test/fxunit
 * @run
 */

import javafx.fxunit.*;

public class BoundSelectTest extends FXTestCase {
    function testBoundSelect() {
        var f1 = Foo { i: 3 }
        var f2 = Foo { i: 5 }
        var bfi = bind f1.i;
        assertEquals(3, bfi);
        f1.i = 9;
        assertEquals(9, bfi);
        f1 = f2;
        assertEquals(5, bfi);
    }
}

class Foo {
    attribute i : Integer;
}
