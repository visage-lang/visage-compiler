/*
 * @test/fxunit
 */

import javafx.fxunit.*;

class Foo {
  var a : Integer;
}

class jfxc945 extends FXTestCase {
  function testDBZ() {
    var a = 4;
    var b = 2;
    var c = bind a / b;
    assertEquals(2, c);
    b = 0;
    assertEquals(0, c);
    b = 4;
    assertEquals(1, c);
  }

  function testNPE() {
    var f : Foo;
    var v = bind f.a;
    assertEquals(0, v);
    f = Foo { a: 8 }
    assertEquals(8, v);
  }

  function testAIOOBE() {
    var i = 0;
    var s : Integer[] = [ ];
    var v : Integer = bind s[i];
    assertEquals(0, v);
    insert 3 into s;
    assertEquals(3, v);
  }

}
