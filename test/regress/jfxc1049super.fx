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

class Alpha {
    public function foo() {"Alpha"}
    public bound function boo() {"Balpha"}
}

class Beta extends Alpha {}

class ThreeGamma extends Two, Beta {
    public function foo() {
        "One: {Two.foo()} Alpha: {Beta.foo()}";
    }
    public bound function boo() {
        "Bone: {Two.boo()} Balpha: {Beta.boo()}";
    }
}

var tg = ThreeGamma{}
System.out.println(tg.foo());
System.out.println(tg.boo());
