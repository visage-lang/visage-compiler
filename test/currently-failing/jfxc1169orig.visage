/**
 * Regression test JFXC-1169 : Compiler crashes on a Bind to a fx class instance creation with incorrect const args
 *
 * @test/fail
 */

class InvalidConstArgTest{
  var x:Integer;
  var y:Integer;
}
var z = bind new InvalidConstArgTest(1); // Invalid call
