/**
 * Regression test JFXC-3643 : compiled-bind : compiler crash when processing FontConverter.fx -- lower: sequences/if/throw
 *
 * @test
 */

function convertFromString(property: String) {
    if (property == "font") {
        ["hi"]
    } else {
        throw new java.lang.UnsupportedOperationException("");
    }
    println("----")
}