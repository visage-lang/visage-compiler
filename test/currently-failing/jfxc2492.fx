/**
 * JFXC-2492 : Runtime: conversions between bound numeric sequences
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

def bb : Byte[] = bind fs;
def lb : Long[] = bind ds;
def fb : Float[] = bind ds;
def db : Double[] = bind ls;

ds = dc;
println( fb );
println( lb );
ls = lc;
println( db );
fs = fc;
println( bb );
