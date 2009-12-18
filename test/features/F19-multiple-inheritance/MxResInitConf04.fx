/*
 * Test for the initializer conflicts resolution.
 *
 * @test/fxunit
 */

import javafx.fxunit.FXTestCase;

mixin class Mixin1 { public var bar : String }
mixin class Mixin2 { public var bar : String = "M2" }
class Mixee extends Mixin1, Mixin2 { override public var bar : String = "" }

public class MxResInitConf04 extends FXTestCase {
    /*
     * All parents and a Mixee declare the variable bar,
     * with a def.val. in Mixin2 and no default value in
     * Mixin1 and Mixee.
     */
    function testConflictResolution() {
        var m = Mixee {};
        /* Uncomment when JFXC-3072 is fixed */
//        assertEquals("", m.bar);
    }
}
