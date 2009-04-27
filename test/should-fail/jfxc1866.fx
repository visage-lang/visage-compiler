/**
 * Should-fail test JFXC-1866 : Compiler crash: atempting to pass constructor argument to JavaFX class in 'new'
 *
 * @test/compile-error
 */

class Sample {}
new Sample(1);
