/*
 * Test for unresolvable conflicts.
 * Super and Mixin declare an override-incompatible function.
 *
 * NOTE: please change the test tag to (at)test/compile-error and
 *       check the expected output when JFXC-3127 is fixed.
 *
 * @test/fail
 */

public mixin class Mixin {
    public function foo(arg : Integer) : String { "" }
}

class Super {
    public function foo(arg : Integer) : Integer { 0 }
}

class Mixee extends Super, Mixin {}
