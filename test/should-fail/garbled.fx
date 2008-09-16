/**
 * Ensure that garbled statements and declarations are caught and recovered from by the parser.
 * 
 * @test/compile-error
 */

package jfx1;


class A {
    va
    function f() {
    }
}
if (A) {

    var ff;
    *9;
    *9;
    var lll;
}

