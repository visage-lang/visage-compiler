/*
 * Regression test JFXC-1087 : bound interpolator
 *
 * @test
 * @run
 */

import java.lang.System;

class One {
    public function foo() {"One"}
    public bound function boo() {"Bone"}
}

class Two extends One {}

mixin class Alpha {
    public function foo() {"Alpha"}
    public bound function boo() {"Balpha"}
}

mixin class Beta extends Alpha {}

class ThreeGamma extends Two, Beta {
    override function foo() {
        "One: {Two.foo()} Super: {super.foo()}";
    }
    override bound function boo() {
        "Bone: {Two.boo()} Buper: {super.boo()}";
    }
}

var tg = ThreeGamma{}
System.out.println(tg.foo());
System.out.println(tg.boo());
