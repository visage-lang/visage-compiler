/**
 * Should-fail test JFXC-1891 : public-init access modifier should not be applied to def variable
 *
 * @test/compile-error
 */

public-init def max=100;

class Foo {

   public-init def min=1;

}
