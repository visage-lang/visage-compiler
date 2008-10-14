/**
 * Regression test JFXC-2140 : 'and' behaving differently when used with bind
 * Regression test JFXC-2141 : OR opertor behaves differently in bind and non-binding contexts.
 *
 * @test
 * @run
 */

class A {
  function notso() : Boolean { false }
  function isso() : Boolean { true }
}

function bad() : Boolean { println("Oops"); true }
function good(say : String) : Boolean { println(say); true }

var x = new A;
var ba = bind x.notso() and bad();
var bo = bind x.isso() or bad();
var ga = bind x.isso() and good('cool');
var go = bind x.notso() or good('excellent');
x = new A;

var nice = 'nice';
var boss = 'boss';
var cba = bind java.lang.Boolean.FALSE and bad();
var cbo = bind java.lang.Boolean.TRUE or bad();
var cga = bind java.lang.Boolean.TRUE and good(nice);
var cgo = bind java.lang.Boolean.FALSE or good(boss);
nice = 'so nice';
boss = 'keen';

function bbad() : java.lang.Boolean { println("Oops"); java.lang.Boolean.TRUE }
function ggood(say : String) : java.lang.Boolean { println(say); java.lang.Boolean.TRUE }
var y = new A;
var jba = bind y.notso() and bbad();
var jbo = bind y.isso() or bbad();
var jga = bind y.isso() and ggood('wow');
var jgo = bind y.notso() or ggood('radical');
y = new A;

println("Done");
