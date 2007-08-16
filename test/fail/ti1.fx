// Incorrectly fails to compile -- Issue 19
import java.lang.System;

    public class Bar {
        readonly attribute a = 3;
        private attribute b = bind a * 10;
        function f1(x, y, z) { x - y }
        public function f2(a, b, c)  { a + b <> 6; }
    }

    class Bat extends Bar {
        attribute rr : Bar =  Bar { a : 88 };
    }

    var tt = 9;
    var v1 : Number;
    var v2 : Integer = tt * tt / 5;
    var v3 = bind v2;
    var v4 = bind lazy v1;
    v1 = 6.7;
    v1 += v2;

    var bat = Bat { a: 12 };
    bat.rr.f1(2, 3, 4);