/*
 * JFXC-289: Incorrect code generated for foreach in return position
 *
 * @test
 * @run
 */
import java.lang.System;

function factors(n:Number):Number[] {
   for (i in [1..n/2 step 1.0] where n mod  i == 0) i;
}
for (i in factors(12))
  System.out.println("factor->{i}");
