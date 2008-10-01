/*
 * Test of the temporary error-message until JFXC-1345 is fixed.
 *
 * @test/compile-error
 *
 */

import java.lang.Exception;

// this should compile
try {
    
}
catch (ex1: Exception) {
    
}

// this should compile
var a = {
    try {
        
    }
    finally {
        
    }
    42
}

// this should print a "currently not implemented"-error
var b = {
    try {
        
    }
    catch (ex2: Exception) {
        
    }
    42
}

// test case from jfxc-1345, should also print a "not implemented error"
var mySeq :String [] = {
    var arr: String [];
    try{
        arr=for(v:String in ["s"]){v}
    }
    catch(e){
        e.printStackTrace();
    }
    arr;
};

var makeItALocation = bind b;

