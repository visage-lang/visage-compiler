/**
 * Regression test for JFXC-2504 : break on for-loop with multiple in-clauses only breaks from inner-most in-clause
 *
 * @test
 * @run
 */

for (x in [0..2], y in [0..1]) {
    if (y == 1) {
        break;
    }
    println("x: {x} y: {y}");
}
