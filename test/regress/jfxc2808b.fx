/**
 * JFXC-2808 : Regression: bind to a field with a trigger causes the trigger to get fired twice
 *
 * @test
 * @run
 */

function b(st : String, v : Boolean) : Boolean { print(st); print(" : "); println(v); v }

var cond = false;
var r = false;
var s = false;

def bc = bind if (b("cond", cond)) b("then", r) else b("else", s);

println("-Then");
r = true;
println("Cond+");
cond = true;
println("-Else");
s = true;
println("Cond-");
cond = false;
println("-Then");
r = false;
println("Cond+");
cond = true;
println("-Else");
s = false;
println("+Then");
r = true;
println("Cond-");
cond = false;
println("-Then");
r = false;
println("+Else");
s = true;
