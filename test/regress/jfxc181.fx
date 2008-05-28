/*
 * @test
 */

class Bar {
    attribute a : Integer;
}

class Bar1 {
    attribute aa : Number;
    function fun() : Void {
    }
}

class Bar1Bar extends Bar, Bar1 {
    function b() {
	a = a + 1;
        aa = aa + 1;
        fun();
    }
}
