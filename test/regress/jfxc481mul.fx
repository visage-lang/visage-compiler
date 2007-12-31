/*
 * Regression test: Test of having a multiplication operator in a sequence index
 *
 * @test
 * @run
 */

import java.lang.System;

public class MyClass {
  public attribute foo:String = "bar";
}

public class jfxc481mul /*extends junit.framework.TestCase*/ {
  attribute x:Integer = 2;
  attribute y:Integer = 1;
  attribute seq:MyClass[] = [MyClass{}, MyClass{},MyClass{}];
  function boo(a : Integer) : MyClass { MyClass{} }

  public function test() {
    boo(x).foo = "yyy";
    seq[y].foo = "www";
    seq[x * y].foo = "zzz";
//    assertEquals(seq[2].foo, "zzz")
  }
}

// delete below to make a junit test, and reinstate commented out code
var app = jfxc481mul{};
app.test();

System.out.println(app.seq[2].foo)

