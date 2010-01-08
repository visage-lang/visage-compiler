/*
 * Regression test 
 * JFXC-3881 : Compiled bind optimization: sequence element access through elem$, not Sequence.get
 *
 * Out of range access
 *
 * @test
 * @run
 */

def bi = bind for (i in [0..4]) i;
println(bi[50]);
println(bi[5]);
println(bi[-1]);
println(bi[-100]);

def bn = bind for (i in [0..4]) i * 3.5;
println(bn[50]);
println(bn[5]);
println(bn[-1]);
println(bn[-100]);

def bs = bind for (i in [0..4]) "i: {i}";
println(bs[50]);
println(bs[5]);
println(bs[-1]);
println(bs[-100]);

def bd = bind for (i in [0..4]) i * 1s;
println(bd[50]);
println(bd[5]);
println(bd[-1]);
println(bd[-100]);

