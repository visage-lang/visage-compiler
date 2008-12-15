/**
 * JFXC-2442 : Runtime: conversions between numeric sequences
 *
 * @test/fail
 */

var bs : Byte[];
var ls : Long[];
var fs : Float[];
var ds : Double[];

def bc : Byte[] = [6, 7, 8];
def lc : Long[] = [4343, 1212];
def fc : Float[] = [2.5, 9.1];
def dc : Double[] = [3.14159265358979323846, 1.4142135623730950488];

bs = dc;
println( bs );
ls = fc;
println( ls );
fs = ds;
println( fs );
fs = bs;
println( fs );
ds = fs;
println( ds );
ds = ls;
println( ds );
