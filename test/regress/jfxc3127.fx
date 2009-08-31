/**
 * Regress test for JFXC-3071: mixins: found FloatVariable required IntVariable
 *
 * @test
 */

mixin class Mixin {
    public function foo(arg : Integer) : String { "" }
}

class Super {
    public function foo(arg : Integer) : String { "" }
}

class Mixee extends Super, Mixin {}
