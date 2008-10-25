/**
 * Should-fail test JFXC-1868 : Non-runnable scripts shouldn't be runnable (shouldn't have run, javafx$run$ or main)
 *
 * @test
 * @run/fail
 */

public class Foo {
  // Whatever
}

var x = Foo{};
