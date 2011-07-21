/*
 * Regression test
 * JFXC-3994 : Missing error message when variable override non-existent symbol whose clashes with an outer var
 *
 * @test/compile-error
 */

class jfxc3994a {
    var x  = "";
}

class A extends jfxc3994a {
    override var x = "a";
}

class B {
    override var x = "";
}
