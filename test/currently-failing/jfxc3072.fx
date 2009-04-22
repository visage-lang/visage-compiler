/*
 * Compilation error(s):
 *
 * jfxc3072.fx:36: $bar is already defined in jfxc3072.Mixee1
 * class Mixee1 extends Mixin1, Mixin2 {}
 * ^
 * error: applyDefaults$bar(jfxc3072.Mixee1) is already defined in jfxc3072.Mixee1
 *
 * jfxc3072.fx:30: reference to bar is ambiguous, both variable bar in jfxc3072.Super and variable bar in jfxc3072.Mixin1 match
 *     var far = bar;
 *               ^
 * jfxc3072.fx:31: reference to foo is ambiguous, both function foo() in jfxc3072.Super and function foo() in jfxc3072.Mixin1 match
 *     function moo() { foo() }
 *                      ^
 *
 * Please uncomment corresponding lines in the following tests
 * when this issue is resolved:
 *  test/features/F19-multiple-inheritance/MxMemConfResol01.fx
 *  test/features/F19-multiple-inheritance/MxMemConfResol02.fx
 *  test/features/F19-multiple-inheritance/MxResInitConf02.fx
 *  test/features/F19-multiple-inheritance/MxResInitConf03.fx
 *  test/features/F19-multiple-inheritance/MxResInitConf04.fx
 *
 * @test/fail
 */

mixin class Mixin1 {
    public var bar : Integer;
    public function foo() : String { "" }
}

mixin class Mixin2 {
    public var bar : Integer;
    public function foo() : String { "" }
}

class Super {
    public var bar : Integer;
    public function foo() : String { "" }
}

class Mixee1 extends Mixin1, Mixin2 {}
class Mixee2 extends Super, Mixin1 {
    var far = bar;
    function moo() { foo() }
}
