/*
 * Feature test for lazy bind: bind to member
 *
 * @test
 * @run
 */

println("==lazy member bind");

class Foo {
    var x: Integer;
    var y: Integer;
}

var foo = Foo { x: 1, y: 2 };

var fx = bind lazy foo.x
    on replace ov { println("fx {ov} => {fx}") };
var fxy = bind lazy foo.x + foo.y
    on replace ov { println("fxy {ov} => {fxy}") };

pfx();
pfxy();
println("modify x");
foo.x = 9;
pfx();
pfxy();
println("modify y");
foo.y = 10;
pfx();
pfxy();
println("modify foo");
foo = Foo { x: 100, y: 200 };
pfx();
pfxy();

function pfx() { println("reading fx"); println("fx:{fx}") }
function pfxy() { println("reading fxy"); println("fxy:{fxy}") }