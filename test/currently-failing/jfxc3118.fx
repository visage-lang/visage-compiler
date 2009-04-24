/*
 * @test
 * @run/fail
 * 
 */

// When this isuue will be fixed, please, update test
// features/F25-lazy-bind/Lz/LzGlobalVarSec.fx

var test1 : Character[] = [60, 61];
if (test1 != [60, 61]) println("FAILED");