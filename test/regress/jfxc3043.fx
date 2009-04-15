/**
 * Regress test for JFXC-3043 - Mixin member initilisations are not done before
 * mixee members are initilized.
 *
 * @test
 * @run
 */

mixin class A { 
    protected def msg:String = "Hi, there!"; 
}

class B extends A { 
    public var url : String = null on replace { 
        java.lang.System.out.println("Message from mixin: {msg}"); 
    } 
} 

var x = new B();
