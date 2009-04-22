/*
 * Compilation error:
 *
 * jfxc3095.fx:20: An override variable cannot be given a type as that comes from the variable you override.
 *     override public var bar : String;
 *                               ^
 * 1 error
 *
 * Please uncomment corresponding lines in the following tests
 * when this issue is resolved:
 *  test/features/F19-multiple-inheritance/MxResInitConf01.fx
 *  test/features/F19-multiple-inheritance/MxResInitConf02.fx
 *  test/features/F19-multiple-inheritance/MxResInitConf03.fx
 *  test/features/F19-multiple-inheritance/MxResInitConf04.fx
 *
 * @test/fail
 */

mixin class Mixin {
    public var bar : String;
}

class Mixee extends Mixin {
    override public var bar : String;
}
