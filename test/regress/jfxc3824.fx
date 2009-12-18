/**
 * JFXC-3824 : Compiler crashes when compiling a code containing a 'getClass().getResource()' statement bound to a var.
 *
 * @test
 */

// This used to crash in the back-end with "incompatible types".

class CSSTest { 
    init { 
        var css1 = bind getClass().getResource("CSSTest.fx").toString(); 
    } 
} 
