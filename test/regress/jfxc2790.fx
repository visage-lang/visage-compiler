/**
 * JFXC-2790 : Internal error in compiler when 'reverse' used on expression other than the sequence
 *
 * @test
 * @run
 */

var v = "v";
def x = reverse v; 
println(x);

def bx = bind reverse v;
println(bx);
v = "value";
println(bx);
