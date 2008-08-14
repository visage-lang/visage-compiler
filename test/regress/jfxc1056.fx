/*
 * @test
 * @run
 */
import java.lang.System;
class Foo {
    attribute text : String;
    public attribute func: function(): String = myfunc;
    public function myfunc(): String {text}
}
var foo = Foo { text: "Hello" };
System.out.println("foo.func()->{foo.func()}");
