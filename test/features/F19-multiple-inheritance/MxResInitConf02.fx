/*
 * Test for the initializer conflicts resolution.
 *
 * @test/fxunit
 */

import javafx.fxunit.FXTestCase;

mixin class Mixin { public var bar : String = "M" }
class Super { public var bar : String = "S" }
/* Uncomment when JFXC-3095 and JFXC-3072 is fixed */
//class Mixee extends Super, Mixin { override public var bar : String }
class Mixee extends Super, Mixin {}

public class MxResInitConf02 extends FXTestCase {
    /*
     * Mixee, mixin and super declare the variable bar,
     * with a def.val. in mixin and super and no def.val. in mixee.
     */
    function testConflictResolution() {
        var m = Mixee {};
        /* Uncomment when JFXC-3072 is fixed */
//        assertEquals("S", m.bar);
    }
}
