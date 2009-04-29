/*
 * @test
 * @run/fail
 * 
 */

// When this isuue will be fixed, please, update tests
// features/F25-lazy-bind/Lz/lzClassInitSec.fx
// features/F25-lazy-bind/Lz/lzClassVarSec.fx
// features/F25-lazy-bind/Lz/lzGlobalVarSec.EXPECTED

var value : Byte[] = [120 as Byte, 121 as Byte] as Byte[];
def binded1 : Byte[] = bind lazy value;
var binded2 : Byte[] = bind binded1;
insert 121 into value;
var trash : Byte[] = binded2;
def expected : Byte[] = [120 as Byte, 121 as Byte, 121 as Byte] as Byte[];
if (trash != expected) println("FAILED: expected: {expected} received:{trash}");
