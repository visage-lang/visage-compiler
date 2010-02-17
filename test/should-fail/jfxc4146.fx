/**
 * Regress test for JFXC-4146.
 *
 *  Add a flag to get forward reference warning in on replace/bind
 *
 * @compilearg -XDfwdRefOpt=trigger
 * @test/warning
 */

class A {
var a = 1;
}

class B extends A {
var b = 2;
override var a = 3 on replace { b };
}

var x on replace { y };
var y;
