/*
 * Passing long value to a function
 *
 * After fixing please uncomment in test\features\F26-numerics\MethodOverload.fx
 *
 * @test/fail
 */
function foo(x : Long) {}

public class jfxc2571 {
    function test() {
        foo(2200000000 as Long);
        foo(2200000000);
    }
}

