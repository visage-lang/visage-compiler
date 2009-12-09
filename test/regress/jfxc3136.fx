/*
 * JFXC-3136 : Regression: Lazy binding with for loop crashes during runtime
 *
 * @test
 * @run
 * 
 */

var seq1 = [1..10];
def seq2 =  bind lazy for (item in seq1) item*2;
println(seq2);
