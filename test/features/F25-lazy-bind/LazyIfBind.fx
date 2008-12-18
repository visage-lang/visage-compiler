/*
 * Feature test for lazy bind: bind to if
 *
 * @test
 * @run
 */

var bool : Boolean = true;
var x : Integer = 1
        on replace ov { println("x {ov} -> {x}") };
var y : Integer = 2
        on replace ov { println("y {ov} -> {y}") };
var z : Integer = bind lazy if (bool) then x else y
        on replace ov { println("z {ov} -> {z}") };

println("==lazy integer if");
pz();
pz();
println("modify x");
x = 9;
pz();
println("modify y");
y = 3;
pz();
println("modify b");
bool = false;
pz();

function pz() { println("reading z"); println("z:{z}") }

/*
println("==lazy sequence if");
bool = true;
var xs : Integer[] = [ 1, 2 ]
    on replace oldX[a..b]=newElements { println("x[{a}..{b}] => [ { newElements } ]") };
var ys : Integer[] = [ 3, 4 ]
    on replace oldY[a..b]=newElements { println("y[{a}..{b}] => [ { newElements } ]") };
var zs : Integer[] = bind lazy if (bool) then xs else ys
    on replace oldZ[a..b]=newElements { println("z[{a}..{b}] => [ { newElements } ]") };

function pzs() { println("reading zs"); println("zs:{zs}") }

pzs();
pzs();
println("modify xs");
insert 4 into xs;
pzs();
println("modify ys");
insert 5 into ys;
pzs();
println("modify b");
bool = false;
pzs();
*/
