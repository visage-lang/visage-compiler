/*
 * Regression test JFXC-1339 : Compiler crashes with for loop in Finally block
 *
 * @test
 * @run
 */

import java.lang.System;

try {
}
finally
{
        for ( i : Integer in
        [
        {
                var square :Integer;
                for (j : function ( x: Integer ) : Integer in [ function ( y: Integer ) : Integer { y*y} ] ) {
                        square =j(4)
                };
                square
        }
        ]
        ){ System.out.println("{i}"); }
} 