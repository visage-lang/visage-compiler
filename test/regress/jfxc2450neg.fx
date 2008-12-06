/**
 * Regression test JFXC-2450 : Translation support for new runtime bound operator implementation
 *
 * @test
 * @run
 */

var by : Byte = 1;
var sh : Short = 10;
var ii : Integer = 100;
var lo : Long = 2147383649;
var fl : Float = 1234.5;
var db : Double = 6.12e40;
var bo : Boolean = true;

def mby = bind -by;
def msh = bind -sh;
def mii = bind -ii;
def mlo = bind -lo;
def mfl = bind -fl;
def mdb = bind -db;
def nbo = bind not bo;

println( mby );
println( msh );
println( mii );
println( mlo );
println( mfl );
println( mdb );
println( nbo );


