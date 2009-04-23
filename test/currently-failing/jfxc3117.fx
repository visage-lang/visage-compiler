/*
 * JFXC-3117: Unable to use 'this' in mixin method body
 *
 * Update the following test when this is fixed:
 *  test/features/F19-multiple-inheritance/MxThisKw01.fx
 *
 * @test/fail
 */

/* Remove 'mixin' to compile successfully */ 
mixin class Mx {
    function foo() {}
    function bar() {
        this.foo()
    }
}
