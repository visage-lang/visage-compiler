/**
 * Regression test for JFXC-869 - Visibility of catch-parameter
 * This script should compile without any error.
 *
 * @test
 * @compile jfxc869.fx
 */

try {
}
catch (ex: java.lang.NullPointerException) {
}
catch (ex: java.lang.Exception) {
}
