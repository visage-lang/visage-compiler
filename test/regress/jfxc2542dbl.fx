/**
 * JFXC-2542 : Proper literal conversions
 *
 * @test
 * @run
 */

def uniu = 1.59486e55; 
def piu = 3.14159265358979;
def unif : Float = 1.59486e55;
def pif : Float = 3.14159265358979;
def xs : Object[] = [piu, unif, pif, uniu];

println("piu = {piu} is boxed as {xs[0].getClass().getName()}");
println("pif = {pif} is boxed as {xs[2].getClass().getName()}");
println("uniu = {uniu} is boxed as {xs[3].getClass().getName()}");
println("unif = {unif} is boxed as {xs[1].getClass().getName()}");

