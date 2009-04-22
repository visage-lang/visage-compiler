/*
 * Test for the initializer conflicts resolution.
 *
 * @test/fxunit
 */

import javafx.fxunit.FXTestCase;

mixin class Mixin1 { public var bar : String = "M1" }
/* Uncomment this when JFXC-3095 is fixed */
//mixin class Mixin2 extends Mixin1 { override public var bar : String }
mixin class Mixin2 extends Mixin1 { override public var bar }


/* Uncomment this when JFXC-3095 is fixed */
//class Mixee1 extends Mixin1 { override public var bar : String }
class Mixee1 extends Mixin1 { override public var bar }
class Mixee2 extends Mixin2 {}

public class MxResInitConf01 extends FXTestCase {
    /*
     * Both mixee and mixin declare the variable bar,
     * with a def.val. in mixin and no def.val. in mixee.
     */
    function testConflictResolution() {
        var m1 = Mixee1 {};
        var m2 = new Mixee2();
        assertEquals("M1", m1.bar);
        assertEquals("M1", m2.bar);
    }
}
