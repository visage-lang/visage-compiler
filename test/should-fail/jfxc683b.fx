/*
 * Regression test: JFXC-683: Use FX type name rather than Java type names in error messages
 *
 * @test/fail
 *
 * This should produce:
 *
 *     jfxc683b.fx:32: incompatible types
 *     found   : String
 *     required: jfxc683b.Node
 *     var n:Node = "";
 *                  ^
 *     1 error
 * 
 * instead of
 * 
 *     jfxc683b.fx:32: incompatible types
 *     found   : java.lang.String
 *     required: jfxc683b.Node
 *     var n:Node = "";
 *                  ^
 *     1 error
 * 
 * There doesn't seem to be a way to provide .EXPECTED files 
 * for tests that are expected to fail compilation.
 *
 */

class Node {
}

var n:Node = "";
