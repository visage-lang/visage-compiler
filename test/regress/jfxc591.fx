/*
 * @test
 * @run
 */

public class Foo {
    public attribute bar: Boolean = true on replace {
        java.lang.System.out.println("replaced bar with {bar}"); 
    }
}

var x = Foo{bar: false}
var y = Foo{bar: true}
