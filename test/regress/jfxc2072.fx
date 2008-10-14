/**
 * Regression test JFXC-2072 : Evaluate rvalue of assignment for setter/getter instance vars when null check aborts assignment
 *
 * @test
 * @run
 */

class Bar {
  var vmem : Integer;
}

function foo() : Integer { println('Here'); 1234 }

var x : Bar = null;

x.vmem = foo();
