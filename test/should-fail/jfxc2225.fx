/*
 * Regression test: JFXC-2225 - Compiler reports Cyclic Inheritence error when there is no Cyclic Inheritence
 *
 * @test/compile-error
 */

class A {}
class jfxc2225 extends A {}

