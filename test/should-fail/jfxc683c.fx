/*
 * Regression test: JFXC-683: Use FX type name rather than Java type names in error messages
 *
 * @test/fail
 *
 * This should produce:
 *
 *     jfxc683c.fx:32: incompatible types
 *     found   : Integer
 *     required: jfxc683c.Node
 *     var n:Node = 1;
 *                  ^
 *     1 error
 * 
 * instead of
 * 
 *     jfxc683c.fx:32: incompatible types
 *     found   : int
 *     required: jfxc683c.Node
 *     var n:Node = 1;
 *                  ^
 *     1 error
 * 
 * There doesn't seem to be a way to provide .EXPECTED files 
 * for tests that are expected to fail compilation.
 *
 */

class Node {
}

var n:Node = 1;
