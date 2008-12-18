/*
 * Feature test for lazy bind: object literal
 *
 * @test
 * @run
 */

var a : Integer = 1
        on replace ov { println("a {ov} -> {a}") };
var b : Integer = 2
        on replace ov { println("b {ov} -> {b}") };

class Foo {
    var x : Integer;
    var y : Integer;
}

var foo : Foo = bind lazy Foo { x: a, y: b }
        on replace ov { println("foo -> {foo.x}, {foo.y}") };

println("==object literal");
pf();
println("modify a");
a = 2;
pf();
println("modify b");
b = 3;
pf();

function pf() { println("reading foo"); println("fx:{foo.x}, fy:{foo.y}") }

println("==object literal initializers");

var moo : Foo = bind Foo { x: bind lazy id(a), y: bind lazy id(b) }
        on replace ov { println("moo replaced") };
pm();
println("modify a");
a = 5;
pm();
println("modify b");
b = 10;
pm();

function pm() { println("reading moo"); println("mx:{moo.x}, my:{moo.y}") }
function id(x:Integer) { println("id{x}"); x }
