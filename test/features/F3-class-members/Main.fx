/* Feature test #3 -- attributes and functions
 * Demonstrates FX class with attributes and functions, and binding to attributes
 * @test
 * @run
 */

import java.lang.*;

class Foo {
    attribute a : String ;
    attribute b : String = bind a;
    attribute c : String ;
    function bleep() : String {
        return "roll";
    }
    function mud() : Integer {
        System.out.println(this.a);
        return 0;
    }
}

var fo : Foo = new Foo();
var ho : Foo = new Foo();

fo.a = "shake";
fo.c = "rattle";
ho.a = "rats";

ho.mud();
ho.mud();
fo.mud();
fo.mud();

System.out.print("fo.a: ");
System.out.println(fo.a);

System.out.print("fo.c: ");
System.out.println(fo.c);

System.out.print("fo.b: ");
System.out.println(fo.b);

System.out.print("ho.b: ");
System.out.println(ho.b);

fo.a = fo.bleep();

System.out.print("fo.a: ");
System.out.println(fo.a);

System.out.print("fo.c: ");
System.out.println(fo.c);

System.out.print("fo.b: ");
System.out.println(fo.b);

