/**
 * Regression test JFXC-1054 : Compiler failure upcasting function type in bound context
 *
 * @test
 * @run
 */

import java.lang.System;

class Foo {
    public attribute selectAction: function(tag:String):Void;
}

var select = function(url:String):Void {
    System.out.println("url: {url}")
}

var foos = bind for (i in [1..10])
  Foo {
       selectAction: select
  };

System.out.println("Built.");
foos[1].selectAction("Done");

