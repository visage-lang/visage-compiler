/**
 * regression test:  JFXC-2837 - Mixins: script-private vars no longer hidden -- var with same name as var in subclass, but with different type fails
 * @test
 * @compilefirst jfxc2837a.fx
 */

public class jfxc2837 extends jfxc2837a {
    var responseHeader: Boolean;
}
