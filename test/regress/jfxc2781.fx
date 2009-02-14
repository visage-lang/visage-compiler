/**
 * JFXC-2781 : Determine rules and enforce numeric equality with values statically typed as Object
 *
 * @test
 * @run
 */

def ob : Object = 5 as Byte;
def os : Object = 5 as Short;
def oi : Object = 5 as Integer;
def ol : Object = 5 as Long;
def of : Object = 5 as Float;
def od : Object = 5 as Double;
def oc : Object = 5 as Character;
def ox : Object = null;

def b : Byte = 5;
def s : Short = 5;
def i : Integer = 5;
def l : Long = 5;
def f : Float = 5;
def d : Double = 5;
def c : Character = 5;

println(7 == oi);
println(od == 6.6);
println(ox == 3);

println(b == ob);
println(b == os);
println(b == oi);
println(b == ol);
println(b == of);
println(b == od);
println(b == oc);

println(s == ob);
println(s == os);
println(s == oi);
println(s == ol);
println(s == of);
println(s == od);
println(s == oc);

println(i == ob);
println(i == os);
println(i == oi);
println(i == ol);
println(i == of);
println(i == od);
println(i == oc);

println(l == ob);
println(l == os);
println(l == oi);
println(l == ol);
println(l == of);
println(l == od);
println(l == oc);

println(f == ob);
println(f == os);
println(f == oi);
println(f == ol);
println(f == of);
println(f == od);
println(f == oc);

println(d == ob);
println(d == os);
println(d == oi);
println(d == ol);
println(d == of);
println(d == od);
println(d == oc);

println(c == ob);
println(c == os);
println(c == oi);
println(c == ol);
println(c == of);
println(c == od);
println(c == oc);

println(ol == s);

println(ox == 0);
println(ox == 0.0);

println(0 == ox);
println(0.0 == ox);
