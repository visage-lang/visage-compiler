/*
 * Test for the initializer conflicts resolution.
 *
 * @test/fxunit
 */

import javafx.fxunit.FXTestCase;

mixin class Mixin1 { public var bar : String = "M1" }
mixin class Mixin2 { public var bar : String = "M2" }
class Super { public var bar : String }
/* Uncomment when JFXC-3095 and JFXC-3072 is fixed */
//class Mixee extends Super, Mixin1, Mixin2 { override public var bar : String }
class Mixee  { }


public class MxResInitConf03 extends FXTestCase {
    /*
     * All parents and a mixee declare the variable bar, 
     * with a def.val. in Mixin1 and Mixin2 and
     * no default value in mixee and super.
     */
    function testConflictResolution() {
        var m = Mixee {};
        /* Uncomment when JFXC-3072 is fixed */
//        assertEquals("", m.bar);
    }
}
