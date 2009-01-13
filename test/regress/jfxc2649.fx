/**
 * JFXC-2649 : Cute2Player doesn't compile - regression - in nested for loop in a bind
 *
 * @test
 * @run
 */

class A {}
class B extends A {
   override function toString() : String { "B" }
}

var q : A[] = bind for (y in [0..1]) for (x in [0..1]) B{};
println(q);

