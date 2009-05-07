/**
 * JFXC-3218 : Compiler throws internal error when compiling the UI control code
 *
 * @compilefirst jfxc3218A.fx
 * @test
 * @run
 */

import jfxc3218A.*;

class jfxc3218 {

init {
     a = "Good";
    }
}

def b = jfxc3218{}
println(a);
