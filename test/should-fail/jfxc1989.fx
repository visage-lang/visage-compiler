/**
 * Should-fail test for JFXC-1989.
 *
 * __DIR__ and __FILE__ should be read-only script variables.
 *
 * @test/compile-error
 */


// __DIR__ should not be assignable
__DIR__ = "mydir";

// __FILE__ should not be assignable
__FILE__ = "myfile";
