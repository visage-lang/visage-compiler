/**
 * Regression test JFXC-837 : bound function call within bind
 *
 * @test
 * @run
 */
import java.lang.System; 

var enableBindingOverhaul;

var offset = 1000;
bound function stat(x : Integer) : Integer { x + offset }
var k = 2;
var bs = bind stat(k);
System.out.println("{offset} + {k} = {bs}");
k = 35;
System.out.println("{offset} + {k} = {bs}");
offset = 100;
System.out.println("{offset} + {k} = {bs}");


class Foo {
  attribute val : Number;
  bound function v(scale : Integer) : Number { val * scale }
  attribute next : Foo;
  bound function getNext() : Foo { next }
}

var f4 = Foo {val: 4}
var f100 = Foo {val: 100 next: f4}
var fp : Foo;
var sca = 3;

System.out.println("----  bind fp.v(sca)  ----");
var bf = bind fp.v(sca);
System.out.println("null {sca} = {bf}");
fp = f4;
System.out.println("4 * {sca} = {bf}");
sca = 1;
System.out.println("4 * {sca} = {bf}");
fp = f100;
System.out.println("100 * {sca} = {bf}");
sca = 5;
System.out.println("100 * {sca} = {bf}");
fp = null;
System.out.println("null {sca} = {bf}");

System.out.println("----  fp.next.v(sca)  ----");
var bfn = bind fp.next.v(sca);
System.out.println("null {sca} = {bfn}");
fp = f4;
System.out.println("4->null  {sca} = {bfn}");
sca = 1;
System.out.println("4->null  {sca} = {bfn}");
fp = f100;
System.out.println("100->4 * {sca} = {bfn}");
sca = 5;
System.out.println("100->4 * {sca} = {bfn}");

System.out.println("----  bind fp.getNext().v(sca)  ----");
var bfgn = bind fp.getNext().v(sca);
fp = null;
System.out.println("null {sca} = {bfgn}");
fp = f4;
System.out.println("4->null  {sca} = {bfgn}");
sca = 1;
System.out.println("4->null  {sca} = {bfgn}");
fp = f100;
System.out.println("100->4 * {sca} = {bfgn}");
sca = 5;
System.out.println("100->4 * {sca} = {bfgn}");

