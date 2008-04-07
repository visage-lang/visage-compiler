
/*
 * Regression test: JFXC-1037: report lexical errors 
 *
 * @test/fail
 */


class trial10 { 
    attribute flag: Boolean = false; 
    function f() { 
        if (! flag) { 
            flag = true; 
        } 
    } 
} 

