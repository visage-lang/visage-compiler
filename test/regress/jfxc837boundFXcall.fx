/**
 * Regression test JFXC-837 : bound function call within bind
 *
 * @test
 * @run
 */
import java.lang.System; 

var enableBindingOverhaul;

var offset = 1000;
bound function stat(x : Integer) : Integer { x + offset }
var k = 2;
var bs = bind stat(k);
System.out.println("{offset} + {k} = {bs}");
k = 35;
System.out.println("{offset} + {k} = {bs}");
offset = 100;
System.out.println("{offset} + {k} = {bs}");


class Foo {
  attribute val =  4.0;
  bound function v(scale : Number) : Number { val * val }
}
