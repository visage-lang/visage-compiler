/*
 * Method overloading
 *
 * @test/fxunit
 * @run
 */
import javafx.fxunit.*;

function testOverload(i : Integer) {}
function testOverload(i : Long) {}


public class MethodOverload extends FXTestCase {
    function test() {
        var i : Integer = 55;
        testOverload(i);
    }
}
