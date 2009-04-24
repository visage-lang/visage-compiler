/*
 * @test
 * 
 */

// When this isuue will be fixed, please, update test
// features/F25-lazy-bind/Lz/bdForGlobal.fx

var seq1 : Character[] = [64 as Character, 61 as Character] as Character[];
def seq2 : Character[] = bind for (item in seq1) item + 6; 