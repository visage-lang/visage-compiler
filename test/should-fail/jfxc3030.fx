/*
 * Regression test for JFXC-3030 : Type Casting between unrelated classes causes crach in back-end
 *
 * @test/compile-error
 */

class X {}
class Y {}

var x = X {};
var y = x as Y;
