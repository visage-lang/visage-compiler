/*
 * Regression: JFXC-2284 - Compiler does not exit script for an uncaught Runtime Exception in nested trigger.
 *
 * @test
 * @run
 *
 */

var x = bind 3 on replace {
                 
                 def y = 4 on replace {
                             
               	 throw new java.lang.RuntimeException("uncaught exception") //#Line5
                            }
               println("1.This statement should not be executed if exception thrown. x value {x}");
               println("2.This statement should not be executed if exception thrown. x value {x}");
             }
