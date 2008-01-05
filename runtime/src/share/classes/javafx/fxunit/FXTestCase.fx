package javafx.fxunit;
import java.lang.Object;

public class FXTestCase extends junit.framework.TestCase {
    function assertEquals(a : Object[], b : Object[]) {
        assertEquals(sizeof a, sizeof b);
        for (i in [0..sizeof a -1]) {
            var ax : Object = a[i];
            var bx : Object = b[i];
            assertEquals(ax, bx);
        };
    }
}
