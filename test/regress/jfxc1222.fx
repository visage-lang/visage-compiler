/**
 * regression test: JFXC-1222 : NPE from TreeInfo.skipParens
 *
 * @test
 */

public class SkipParensTest {
    var selectedButton on replace oldSelection {
        if (oldSelection != null) {
        }
    };
}
