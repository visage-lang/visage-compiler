/**
 * Regression test for JFXC-1636 : For now, disallow all blank 'def's
 *
 * @test/fail
 */

def a : Integer;

class Foo {

   def b : String;

   function bar() {
      def c : Foo;
   }
}

