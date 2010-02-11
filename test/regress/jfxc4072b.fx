/*
 * Regression test: Forward reference check overhaul
 *
 * @test
 */

class Base {
    var a:Integer;
    var b:Integer;
    var c:Integer;
    var d:Integer;
    var e:Integer;
    var f:Integer;
    var g:Integer;
    var h:Integer;
    var i:Integer;
    var l:Integer;
    var m:Integer;
    var n:Integer;
}

class A extends Base {
    override var a:Integer = b = 1;
    override var b:Integer = this.c = 1;
    override var c:Integer = A.d = 1;
    override var d:Integer = jfxc4072b.A.e = 1;
    override var e:Integer = e = 1;
    override var f:Integer = f = 1;
    override var g:Integer = A.g = 1;
    override var h:Integer = jfxc4072b.A.h = 1;
    override var i:Integer = { l = 1; A.l = 1; jfxc4072b.A.l = 1; super.l = 1; this.l = 1; i = 1; A.i = 1; jfxc4072b.A.i = 1; super.i = 1; this.i = 1; };
    override var l:Integer = 42;
    override var m = super.n = 1;
    override var n = super.n = 1;
}
