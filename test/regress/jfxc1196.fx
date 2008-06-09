/**
 * regression test: JFXC-1196 : NPE when binding a method from inherited JFX class
 * @test
 * @run
 */

import java.lang.System;

class Foo {
    function getString(): String {
        return "Foo";
    }
    bound function getBoundString(): String {
        return "Boo";
    }
}

class Moo extends Foo {
    function getString(): String {
        var s = bind Foo.getString();
        return "Moo.{s}";
    }
    bound function getBoundString(): String {
        var s = bind Foo.getBoundString();
        return "Moo.{s}";
    }
}
    
System.out.println(Moo{}.getString());
System.out.println(Moo{}.getBoundString());
