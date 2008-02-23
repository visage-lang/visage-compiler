/*
 * @test
 * @run
 */
import java.lang.System;
public class Bug {
    attribute foo:String;

    attribute bar = java.lang.Runnable {
        public function run():Void {
            foo = "Hello World";
        }
    };
}
var b : Bug = Bug { foo: "nada" };
System.out.println("foo->{b.foo}");
b.bar.run();
System.out.println("foo->{b.foo}");
