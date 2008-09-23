/*
 * Regression test JFXC-1838 :  incorrect type inference
 *
 * @test
 * @compile/fail
 */

class Foo {
    var str="Hello";
    var to;
}

var hvf="World";
var foo = Foo {
    to:bind hvf with inverse;
}