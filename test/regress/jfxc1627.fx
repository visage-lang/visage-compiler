/**
 *
 * Regression test JFXC-1627 : Implement 'def'
 *
 * @test
 * @run
 */

import java.lang.System;

def round : String = "Orbital";

class Fuzzy {

   private def count = 1234;

   function doit() {
      def snurd = 3.1415926;
  
      System.out.println("{round} path {snurd} over {count}")
   }
}

(new Fuzzy).doit()
