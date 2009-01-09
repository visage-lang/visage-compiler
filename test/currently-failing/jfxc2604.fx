/*
 * Method overload with Float/Number parameters causes compiler error
 *
 * After fixing please uncomment and update corresponding testcases in
 *     test/features/F26-numerics/NumOverload1.fx
 *
 * @test/compile-error
 */

function overload(x: Float) { }
function overload(x: Number) { } 
