package javafx.fxunit;
import java.lang.Object;
import junit.framework.TestCase;

public class FXTestCase extends TestCase {
    function assertEquals(a : Object[], b : Object[]) {
        assertEquals(sizeof a, sizeof b);
        for (i in [0..sizeof a -1]) {
            var ax : Object = a[i];
            var bx : Object = b[i];
            assertEquals(ax, bx);
        };
    }
    
    function assertEquals(expected:Number, actual:Number) : Void {
        TestCase.assertEquals(expected as Object, actual as Object);
    }
}
