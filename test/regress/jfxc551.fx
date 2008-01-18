/*
 * @test/fxunit
 * @run
 */

class Foo { attribute red : Integer; }

function foo(r:Integer): Foo { 
    Foo { red: r } 
} 

class jfxc551 extends javafx.fxunit.FXTestCase {
    function test() {
        var gray1:Foo = bind foo(1); 
        assertEquals(1, gray1.red);
    }
}

