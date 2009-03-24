/**
 * Should-fail test for JFXC-2949 : Range step goes to zero - runtime divide by zero
 *
 * Eager Integer case
 *
 * @test
 * @run
 */

import java.lang.Exception;

var c = 0;
try {
  var rs = bind [1..4 step c];
  println("BAD");
} catch (exc : Exception) {
  println(exc);
}
