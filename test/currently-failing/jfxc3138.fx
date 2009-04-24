/*
 * @test/fail
 * 
 */

// When this isuue will be fixed, please, update test
// features/F25-lazy-bind/Lz/bdForGlobal.fx

var seq1 : Duration[] = [64s as Duration, 61s as Duration] as Duration[];
def seq2 : Duration[] = bind for (item in seq1) item + 6s;
