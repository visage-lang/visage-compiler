/* Test for initializing shadowed attributes with def value coming from super class,
 * def value coming from this class, and a default value coming from an ObjectLiteral.
 * @test/fail
 */
import java.lang.System;

class Base {
    attribute duplicate = 10;
    attribute foo = 1 on replace (old: Integer) {
        System.out.println("Base.foo={foo}, old={old}");
    }
}

class Subclass extends Base {
    attribute duplicate = 20;
    attribute bar = foo + 10 on replace (old: Integer) {

        System.out.println("Subclass.bar={bar}, old={old}");
    }

} 

var base:Base = new Base;
var subclass:Subclass = new Subclass;

System.out.println("Base.duplicate: {base.duplicate}");
System.out.println("Subclass.duplicate: {subclass.duplicate}");

var sub1 = Subclass {
    duplicate:30;
}
System.out.println("Sub1.duplicate: {sub1.duplicate}");

var base1 = Base {
    duplicate:40;
}
System.out.println("Base1.duplicate: {base1.duplicate}");
