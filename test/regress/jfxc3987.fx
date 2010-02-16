/*
 *
 * Regression : JFXC-3987 - Compiled-bind: bad interaction between toplevel and nested mixins.
 *
 * @test
 *
 */

mixin class jfxc3987 {}

mixin class M extends jfxc3987 {}

class A extends M {}

class B extends M {}
