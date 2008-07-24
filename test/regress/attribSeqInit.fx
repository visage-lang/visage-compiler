/*
 * Regression test to attribute initialized to sequence NPE reported by Jim Clarke
 * @test
 * @run
 */

import java.lang.System;

public class F {
   public readable attribute months = [
       "Jan", "Feb", "Mar",
       "Apr", "May", "Jun",
       "Jul", "Aug", "Sep",
       "Oct", "Nov", "Dec"
   ];
}
System.out.println((new F).months);
