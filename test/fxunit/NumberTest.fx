/*
 * @test/fxunit
 */

import javafx.fxunit.FXTestCase;

public class NumberTest extends FXTestCase {
    public function testAssertEqualsNumber() {
        assertEquals(1.0, 1.0);
    }

    public function testAssertEqualsNumberMessage() {
        assertEquals("message", 2.0, 2.0);
    }
}
