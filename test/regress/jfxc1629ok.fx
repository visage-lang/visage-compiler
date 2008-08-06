/**
 * Regression test for JFXC-1629 : Enforce public-readable modifier
 *
 * Pass cases, see also should-fail
 *
 * @test
 * @run
 */

import java.lang.System;

class One {

  public-readable private var twub = 333;

  function change() : Void {
     twub = 444;
  }

  function make() : One {
     One {twub: 111}
  }
}

var uno = One { };  
System.out.println( uno.twub );

uno.change();
System.out.println( uno.twub );

System.out.println( uno.make().twub );




