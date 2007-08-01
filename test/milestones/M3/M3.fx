// M3.fx
// Demonstrates compilation of FX class with attributes, functions, and operations to Java class
//              attributes have explicit type
//              class-level attribute initializers
//              unidirectional binding of attributes to expressions involving attributes

import java.lang.*;

class Foo {
    attribute a : String ;
    attribute b : String ;
    attribute c : String ;
    function bleep() : String;
    operation mud() : Integer;
}

attribute Foo.b = bind a;

function Foo.bleep() {
    return "roll";
}

operation Foo.mud() {
    System.out.println(this.a);
    return 0;
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

