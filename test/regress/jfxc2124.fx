/**
 * Regression test JFXC-2124 : Function call arguments must be evaluated exactly once, even if null check fails
 *
 * @test
 * @run
 */

class Foo {
  var x : Foo;
  function say(str : String) : String { println("Say: {str}"); str }
  function say(str : String, str2 : String) : String { println("Say2: {str} & {str2}"); str }
  override function toString() : String { "Foo" }
}

function yell(str : String) : String { println("Yell: {str}"); str }

function give(oo : Foo) : Foo { println("Give: {oo}"); oo }

var fo : Foo;

fo.say(yell("One"));
fo.x.say(yell("Two"));
give(null).say(yell("Beans"), yell("Rice"));

fo = Foo{};

fo.say(yell("Won"));
fo.x.say(yell("Too"));
give(fo).say(yell("Earth"), yell("Sky"));
give(fo).x.say(yell("Bird"), yell("Plane"));

fo.x = fo;
fo.x.say(yell("To"));
give(fo).x.say(yell("Turtle"), yell("Car"));
