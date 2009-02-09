/*
 * Passing long value to a function
 *
 * After fixing please uncomment in test\features\F26-numerics\MethodOverload.fx
 *
 * @test
 * @run
 */
function foo(x : Long) {}

function run() {
        foo(2200000000 as Long); 
        foo(2200000000);
}


