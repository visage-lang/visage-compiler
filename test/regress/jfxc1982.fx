/**
 * regression test JFXC-1982 : Eliminate reification of sequence element types
 *
 * @test
 * @run
 */

var intSeq : Integer[] = [1,2,3];
var numSeq : Number[] = [1.0, 2.0, 3.0];
var boolSeq : Boolean[] = [true, false];
var stringSeq : String[] = ["a", "b", "c"];
var objSeq : Object[] = ["a", "b", "c"];
var durSeq : Duration[] = [1s, 2s, 3s];

println("Out of bounds [-1]");
println("int {intSeq[-1]}");
println("num {numSeq[-1]}");
println("bool {boolSeq[-1]}");
println("string /{stringSeq[-1]}/");
println("obj /{objSeq[-1]}/");
println("dur /{durSeq[-1]}/");

intSeq = [];
numSeq = [];
boolSeq = [];
stringSeq = [];
objSeq = [];
durSeq = [];

println("Element access empty sequence");
println("int {intSeq[0]}");
println("num {numSeq[0]}");
println("bool {boolSeq[0]}");
println("string /{stringSeq[0]}/");
println("obj /{objSeq[0]}/");
println("dur /{durSeq[0]}/");

var int : Integer;
var num : Number;
var bool : Boolean;
var string : String;
var obj : Object;
var dur : Duration;

println("Var defaults");
println("int {int}");
println("num {num}");
println("bool {bool}");
println("string /{string}/");
println("obj /{obj}/");
println("dur /{dur}/");
