/**
 * JFXC-751 - init of superclass called too many times when indirectly inherited twice
 *
 * @test
 * @run
 */

mixin class Base { 
    init { 
        java.lang.System.out.println("Base.init called"); 
    } 
} 

class Child extends Base {
    init { 
        java.lang.System.out.println("Child.init called"); 
    } 
} 

class Baby extends Child, Base {
    init { 
        java.lang.System.out.println("Baby.init called"); 
    } 
} 

var baby = Baby{} 
