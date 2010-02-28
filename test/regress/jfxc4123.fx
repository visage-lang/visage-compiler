/**
 * JFXC-4123 : overrides should not make binds eager (non-sequences)
 *
 * @test
 * @run
 */

class A {
  var x = 1;
}

class B extends A {
  function show(y : Integer) : Integer { println("show: {y}"); y }
  var val = 0;
  override var x = bind show(val);
  function test() {
    for (i in [0..10]) {
      val = i
    }
    println(x);
  }
}

B{}.test();
