/**
 * Regression test JFXC-2125 : In a select, components must be evaluated exactly once
 *
 * @test
 * @run
 */

class A {
  var a : String;
  var b : String;
  function say(what : String) : A { println(what); this }
  function nada(what : String) : A { println(what); null }
}

var x = A {a: 'raisins' }
x.say('one').say('two').say('three').b = x.say('up').say('middle').say('down').a;
x.say('one').nada('two').say('oops').b = x.say('yes').nada('fine').say('wrong').a;

