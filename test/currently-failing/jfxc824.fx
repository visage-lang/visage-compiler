/*
 * Regression test for JFXC-824 :Overriding abstract function requires explicit declaration of Void return type
 * @test/fail
 * Should be @test and @run
 * When fixed move this to regress and capture correct output to .EXPECTED file.
 *
 */

abstract class A {
    abstract function foo();
}

class B extends A {
    override function foo(){
        java.lang.System.err.println("Hello");
    }
}

var bRef:A=new B();
bRef.foo();
