/**
 * JFXC-2606 : Compiler error returing an if expr that has a throw in the else part
 *
 * @test
 */

import java.lang.IllegalArgumentException;

class jfxc2602 {
    public function ff():Object {
        if (false)
            "xx"
        else 
            throw new IllegalArgumentException("ff");
    }
}
