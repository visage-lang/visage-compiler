/**
 * Regression test JFXC-2450 : Translation support for new runtime bound operator implementation
 *
 * @test
 * @run
 */

var by : Byte = 1;
var sh : Short = 10;
var ii : Integer = 100;
var lo : Long = 2147383648;
var fl : Float = 1234.5;
var db : Double = 6.12e40;
var bo1 : Boolean = true;
var bo2 : Boolean = false;
var ob1 : Object = new java.util.ArrayList();
var ob2 : Object = new java.util.Random();

def bo1_bo1_eq = bind bo1 == bo1;
def ob1_ob1_eq = bind ob1 == ob1;
println( bo1_bo1_eq );
println( ob1_ob1_eq );

def bo1_bo1_ne = bind bo1 != bo1;
def ob1_ob1_ne = bind ob1 != ob1;
println( bo1_bo1_ne );
println( ob1_ob1_ne );

def bo1_bo2_eq = bind bo1 == bo2;
def ob1_ob2_eq = bind ob1 == ob2;
def bo1_ob2_eq = bind bo1 == ob2;
def sh_ob2_eq = bind sh == ob2;
def lo_ob2_eq = bind lo == ob2;
println( bo1_bo2_eq );
println( ob1_ob2_eq );
println( bo1_ob2_eq );
println( sh_ob2_eq );
println( lo_ob2_eq );

def bo1_bo2_ne = bind bo1 != bo2;
def ob1_ob2_ne = bind ob1 != ob2;
def bo1_ob2_ne = bind bo1 != ob2;
def sh_ob2_ne = bind sh != ob2;
def lo_ob2_ne = bind lo != ob2;
println( bo1_bo2_ne );
println( ob1_ob2_ne );
println( bo1_ob2_ne );
println( sh_ob2_ne );
println( lo_ob2_ne );

