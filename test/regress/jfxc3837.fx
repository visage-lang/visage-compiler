/*
 * JFXC-3837: Need isBound to detect whether a variable is bound prior to attempting to set a value on it
 *
 * @test
 * @run
 */

var x = 1;

function dump(msg:String, foo:Foo) {
   println("{msg}");
   println("{isReadOnly(foo.a)}");
   println("{isReadOnly(foo.b)}");
}

class Foo {
   var a = 1;
   var b = 2;
}

class ExtFoo1 extends Foo {
   override var a = bind x;
}

class ExtFoo2 extends Foo {
   override var a = bind x with inverse;
}

dump("Foo w/o bound init", Foo{});
dump("Foo w/ bound init", Foo{b:bind x});
dump("Foo w/ bidi bound init", Foo{b:bind x with inverse});
dump("ExtFoo1 w/o bound init", ExtFoo1{});
dump("ExtFoo1 w/ bound init", ExtFoo1{b:bind x});
dump("ExtFoo1 w/ bidi bound init", ExtFoo1{b:bind x with inverse});
dump("ExtFoo2 w/o bound init", ExtFoo2{});
dump("ExtFoo2 w/ bound init", ExtFoo2{b:bind x});
dump("ExtFoo2 w/ bidi bound init", ExtFoo2{b:bind x with inverse});
