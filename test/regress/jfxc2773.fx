/*
 * Regression test: JFXC-2914 - Two warning messages when converting Number[] to Integer[]
 *
 * @test/warning
 */

class C {
    var c;
}

function func():Object {
    b.c
}

var a: C = C {
    c: func;
};

var b = C {
    c: a;
};
