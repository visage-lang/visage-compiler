/*
 * Regression test for JFXC-753 : Deferencing an expression with side-effects executes twice -- eg. Foo{}.show()
 *
 * @test
 * @run
 */

import java.lang.System;

class jfxc753 {
   var y = 14;
   function foo() : jfxc753 {
      System.out.println("foo");
      this
   }
   function bar() : Void {
      System.out.println("bar")
   }
   function bazz() : String { "bazz" }
   init { System.out.println("init") }
}

var x = new jfxc753;
var seq = [x, x, x, x, x, x];
var i = 0;
System.out.println(seq[i++].y);
System.out.println(seq[i++].bazz());
seq[i++].bar();
System.out.println(i);
System.out.println(x.foo().y);
System.out.println(x.foo().bazz());
x.foo().bar();
System.out.println(jfxc753{y: 7}.y);
System.out.println(jfxc753{}.bazz());
jfxc753{}.bar();


