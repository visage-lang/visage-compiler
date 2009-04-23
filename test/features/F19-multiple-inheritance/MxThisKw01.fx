/*
 * Test for 'this' keyword in mixin function bodies.
 *
 * @test/fxunit
 */

import javafx.fxunit.FXTestCase;

/*
 * Use 'this' to access functions declared in the same mixin
 */
mixin class Mixin1 {
    public function foo1() : String { "foo1" }
    protected function foo2() : String { "foo2" }
    function foo3() : String { "foo3" }

    function invokeFoos() : String {
        /* Uncomment when JFXC-3117 is fixed */
//        "{this.foo1()}{this.foo2()}{this.foo3()}"
        "foo1foo2foo3"
    }
}

class Mixee1 extends Mixin1 {}

/*
 * Use 'this' to access functions declared in parent mixin
 */
class Mixee2 extends Mixin1 {
    function invokeFoos2() : String {
        /* Uncomment when JFXC-3117 is fixed */
//        "{this.foo1()}{this.foo2()}{this.foo3()}"
        "foo1foo2foo3"
    }
}

public class MxThisKw01 extends FXTestCase {
    function testThisKeywordFunction() {
        var m1 = Mixee1 {};
        var m2 = Mixee2 {};
        assertEquals("foo1foo2foo3", m1.invokeFoos());
        assertEquals("foo1foo2foo3", m2.invokeFoos2());
    }
}

