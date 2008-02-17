/*
 * Regression test: with attr on replace {...}
 *
 * @test
 * @run
 */

import java.lang.System;

class Base {
  attribute a : Integer = 99 on replace { System.out.println("base") }
}

class Sub extends Base {
  with a on replace { System.out.println("sub") }
}

var s = Sub {}
s.a = 5
