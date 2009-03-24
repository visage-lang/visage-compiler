/**
 * Should-fail test for JFXC-2949 : Range step goes to zero - runtime divide by zero
 *
 * Eager Number case
 *
 * @test
 * @run
 */

import java.lang.Exception;

var c = 0.0;
try {
  var rs = bind [3.4 .. 7.7 step c];
  println("BAD");
} catch (exc : Exception) {
  println(exc);
}
