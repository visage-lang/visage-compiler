/**
 * Regression test JFXC-1057 : variable type inferenced to '? extends ...' type
 *
 * @test/fail
 *
 * JFXC-1057 to cover this new failure (it is a Location now)
 *
 * was:
 * *test
 * *run
 */
import java.lang.System;
class Foo {
  attribute title = 'If Only To Have A Name';
  attribute count = 120;
}
var ooh = Foo {};
var classFoo = ooh.getClass();
var jooh = classFoo.newInstance(); 
System.out.println("jooh.title->{jooh.title}");
