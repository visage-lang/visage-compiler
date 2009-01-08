/**
 * JFXC-2616 : Incorrect error message: 'var' overrides member main(java.lang.String[])
 *
 * @compilefirst jfxc2616base.fx
 * @test
 */

class jfxc2616 extends jfxc2616base {
    public-init var main: Number;
}
