/*
 * Feature test for lazy bind: binding to sequences
 *
 * @test
 * @run
 */

// Simple bind

var x = [ 1, 2, 3 ]
    on replace oldX[a..b]=newElements { println("x[{a}..{b}] => [ { newElements } ]") };
var y = bind lazy x
    on replace oldY[a..b]=newElements { println("y[{a}..{b}] => [ { newElements } ]") };

// @@@ Note that true laziness is not exhibited for this case -- it is more expensive to defer updates than to eagerly propagate

println("==simple sequence bind");
py();
println("modify x");
insert 4 into x;
py();

function py() { println("reading y"); println("y:{y}") }

// Bind to sizeof

println("==sizeof bind");
var sizex = bind lazy sizeof x
    on replace ov { println("sizeof x {ov} => {sizex}") };
println("modify x");
x = [ 1, 2, 3 ];
ps();
ps();
insert 4 into x;
ps();

function ps() { println("reading sizex"); println("sizex:{sizex}") }

// Bind to element

println("==element bind");
var x0 = bind lazy x[0]
    on replace ov { println("x[0] {ov} => {x0}") };
println("assign x");
x = [ 1, 2, 3 ];
px0();
px0();
println("insert x");
insert 4 into x;
px0();
println("assign x[0]");
x[0]=9;
px0();
println("assign x");
x = [4..6];
px0();

function px0() { println("reading x[0]"); println("x[0]:{x0}") }

// Bind to member
// @@@ Also not truly lazy -- bug? 

println("==member bind");
class Foo {
    var seq : Integer[];
}
var foo = Foo { seq: [ 1, 2, 3 ] };
var fooDotSeq = bind lazy foo.seq
    on replace oldSeq[a..b]=newElements { println("fS[{a}..{b}] => [ { newElements } ]") };

function pfs() { println("reading fS"); println("fS:{fooDotSeq}") }

println("insert foo.seq");
insert 4 into foo.seq;
pfs();
pfs();
println("assign foo.seq[0]");
foo.seq[0]=9;
pfs();
println("assign foo.seq");
foo.seq = [0..2];
pfs();
println("assign foo");
foo = Foo { seq: [4..6] };
pfs();

// Bind to reverse
// @@@ NYI

/*

println("==reverse bind");
var rev = bind lazy reverse x
    on replace ov { println("rev {ov} => {rev}") };
println("assign x");
x = [ 1, 2, 3 ];
pr();
println("insert x");
insert 4 into x;
pr();
println("assign x[0]");
x[0]=9;
pr();
println("assign x");
x = [4..6];
pr();

function pr() { println("reading rev"); println("rev:{rev}") }

*/

// Bind to range
// @@@ NYI

// Bind to concatenate
// @@@ NYI

// Bind to singleton
// @@@ NYI

// Bind to comprehension
// @@@ NYI

