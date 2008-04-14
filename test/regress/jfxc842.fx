/*
 * Regression test for JFXC-842. Javafxc successfully compiles code that 
 * throws java.lang.VerifyError when run
 *
 * @test
 * @run
 */ 
 
var z:Integer = {
        var a = if(z==10) 12 else 34;
        a;

} 
