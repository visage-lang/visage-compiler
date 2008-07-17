/**
 * regression test: JFXC-1222 : NPE from TreeInfo.skipParens
 *
 * @test
 */

public class SkipParensTest {
    attribute selectedButton on replace oldSelection {
        if (oldSelection != null) {
        }
    };
}
