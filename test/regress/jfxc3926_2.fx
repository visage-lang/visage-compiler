/**
 * JFXC-3926 :  Exception in OpenJavafx compiler fails to build swat samples and applications.
 *
 * @test
 * @run
 */

class A { 
    var f: function():Void; 
} 

function func() { 
    var x; 
    A { 
        f: function() { 
            x = 1; 
        } 
    } 
    if(true) { 
        return Object{}; //CCE here! 
    } 
    if (false) { 
        return "crash";//ok 
    } 
    return Object{}; 
} 

println(func().getClass());

