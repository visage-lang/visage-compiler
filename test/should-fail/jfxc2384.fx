/*
 * Regression test: JFXC-2384: Compiler should disallow function selection by class name in some cases
 *
 * @test/compile-error
 */

class A {
    public override function toString():String { "A" }
    public var a = "A";

    function testA_meth() {
        Object.toString();
    }

    function testA_field() {
        A.a;
    }
}

mixin class M {
    public override function toString():String { "M" }
    public var m = "M";

    function testM_meth() {
        Object.toString();
    }

    function testM_field() {
        M.m;
    }
}

class B extends A, M {
    public override function toString():String { "B" }
    public var b = "B";

    function testB_meth() {
        Object.toString(); //invalid
        A.toString();
        B.toString();
        M.toString();
    }

    function testB_field() {
        A.a;
        B.b;
        M.m;
    }
}

class C extends B {
    public override function toString():String { "C" }
    public var c = "C";

    function testC_meth() {
        Object.toString(); //invalid
        A.toString(); //invalid
        B.toString();
        M.toString(); //invalid
        C.toString();
    }

    function testC_field() {
        A.a; //invalid
        B.b;
        M.m; //invalid
        C.c;
    }
}
