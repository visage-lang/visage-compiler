/**
 * Regression test JFXC-665 : Generate a no-arg and a one-arg ctor, for better integration with Java
 *
 * @test
 * @run
 */

import java.lang.System;
import java.lang.Class;

class Foo {
  attribute title = 'If Only To Have A Name';
  attribute count = 120;
}

var ooh = Foo {};
var classFoo = ooh.getClass();
var jooh : Foo = classFoo.newInstance(); //TODO typed as work-around for JFXC-1057
System.out.println(jooh.title == ooh.title);
System.out.println(jooh.count == ooh.count);
System.out.println(jooh.title);
System.out.println(jooh.count);


