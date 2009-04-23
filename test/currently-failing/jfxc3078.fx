/*
 * @test
 * @run/fail
 *
 * mixin A should be initialized just once
 */
mixin class A           {init{javafx.lang.FX.println("init A");}}
mixin class E extends A {init{javafx.lang.FX.println("init E");}}
class B extends A       {init{javafx.lang.FX.println("init B");}}
class C extends A, B, E {init{javafx.lang.FX.println("init C");}}

C{};
