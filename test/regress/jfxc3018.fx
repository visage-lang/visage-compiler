/**
 * Regress test for JFXC-3018 - Binding chain isn't updated when initialized.
 *
 * @test
 * @run
 */

var a = 1234; 

class SuperB { 
    var c:C; 
} 

class B extends SuperB { 
    var b = 0; 
    override var c = C { b: this } 
} 

class C { 
    var b:B; 
    var c = bind b.b; 
} 

var b = B { 
    b: bind a 
}; 
println(b.c.c); // expect 1234, is 0 
