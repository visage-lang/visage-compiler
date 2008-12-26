/*
 * Compilation fails for a minimal long value:
 * "Long out of range: -9223372036854775808."
 *
 * @test/fail
 */

def l : Long = -9223372036854775808;
