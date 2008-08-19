/**
 * Regression test JFXC-1812 : Problem with inherting protected access from superclass
 *
 * @compile jfxc1812Base.fx
 * @compile jfxc1812One.fx
 * @test/compile-error
 */

public class jfxc1812Two extends jfxc1812Base {
    override function doIt():Void {
        jfxc1812One{}.doIt();
    }
}

