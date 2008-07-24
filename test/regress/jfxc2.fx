/**
 * regression test: JFXC-2 : Inference for operands participating in arithmetic operations
 *
 * @test
 * @run
 */
import java.lang.System; 

    public class Bar { 
        readable attribute a = 3;
        private attribute b = bind a * 10; 
        function f1(x, y, z) { x - y } 
        public function f2(a, b, c) { a + b != 6; } 
    } 


    var tt = 9; 
    var v1 : Number; 
    var v2 : Integer = tt * tt / 5; 
    var v3 = bind v2; 
    v1 = 6.7; 
