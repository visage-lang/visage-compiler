/**
 * Regression test for JFXC-1629 : Enforce readable modifier
 *
 * @test/fail
 */

class One {

  readable private var twub = 333;

  function make() : One {
     twub = 444; // OK
     One {twub: 111} // OK
  }
}

var uno = One { twub: 222 };  // should fail
uno.twub = 666; // should fail


