/**
 * Regress test for JFXC-3563: Compiled bind: bind object literal with object literal locals crashes compiler with "wrong"
 *
 * @test/compile-error
 */

class A {
 var x;
}

var a = bind A { var y; }
