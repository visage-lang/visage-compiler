/*
 * Regress test for JFXC-1258.
 * 
 * Assertion Error thrown by Compiler 
 * when trying to override attribute of 
 * the same class.

 * Should not be able to override var of
 * current class - only super type should
 * be allowed.
 *
 * @test/compile-error
 */

class JFXC1258 {
    var myvar : String = "default value";

    override var myvar on replace {
        println("myvar is changed!");
    }
} 

JFXC1258 { myvar: "new value" }
