/*
 * A mixin contains the abstract function declarations. 
 * The extending mixees provide corresponding function implementations.
 *
 * @test/fxunit
 */

import javafx.fxunit.FXTestCase;

mixin class Mixin1 {
    public abstract function foo() : Integer;
    public abstract function bar() : String;
}

mixin class Mixin2 extends Mixin1 {}

class Mixee1 extends Mixin1 {
    public override function foo() : Integer { 1 }
    public override function bar() : String { "bar1" }
}

class Mixee2 extends Mixin2 {
    public override function foo() : Integer { 2 }
    public override function bar() : String { "bar2" }
}

public class MxDecl04 extends FXTestCase {
    function testMethodImplementations() {
        var m1 = Mixee1 {};
        var m2 = new Mixee2();
        assertEquals(1, m1.foo());
        assertEquals("bar1", m1.bar());
        assertEquals(2, m2.foo());
        assertEquals("bar2", m2.bar());
    }
}

