/*
 * Regression test: JFXC-683: Use FX type name rather than Java type names in error messages
 *
 * @test/fail
 *
 * This should produce:
 *
 *     jfxc683a.fx:38: incompatible types
 *     found   : jfxc683a.Node[]
 *     required: jfxc683a.Node
 *         content: [Node {}]
 *                  ^
 *     1 error
 * 
 * instead of
 * 
 *     jfxc683a.fx:38: incompatible types
 *     found   : com.sun.javafx.runtime.sequence.Sequence<? extends jfxc683a.Node>
 *     required: jfxc683a.Node
 *         content: [Node {}]
 *                  ^
 *     1 error
 * 
 * There doesn't seem to be a way to provide .EXPECTED files 
 * for tests that are expected to fail compilation.
 *
 */

class Node {
}

class Group extends Node {
    attribute content: Node;
}


Group {
    content: [Node {}]
} 