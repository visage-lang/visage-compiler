/*
 * @test
 * @run/fail
 * 
 */

// When this isuue will be fixed, please, update test
// features/F25-lazy-bind/Lz/LzForGlobal.fx

var seq1 = [1..10];
def seq2 =  bind lazy for (item in seq1) item*2;
