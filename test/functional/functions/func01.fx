import java.lang.System;
import java.text.DecimalFormat;

/*
 * @test
 * @run
 */

/*
Expressions may be factored into subroutines called functions, for example:
*/
function z(a:Number, b:Number):Number {
     var x = a + b;
     var y = a - b;
     return sq(x) / sq (y);
}

function sq(n:Number): Number {n * n;}

/** some formatters */
function formatNumber(n:Number):String {
   var df:DecimalFormat = new DecimalFormat("###,###.##");
   df.setMinimumIntegerDigits(1);
	df.setMinimumFractionDigits(0);
   return df.format(n);
}

/*
A function takes the form:
function name (parameterName : parameterType, ...): returnType body
where body can be any expression.
*/
function m   (               ) {}
function m0  ( i : Integer   ) {}
function m02 ( s : String    ) {}
function m1  ( i : Integer   ): Integer{ return i; }
function m11 ( i : Integer   ): String { return i.toString(); }
function m12 ( s : String    ): String { return s;}
function m2  ( ia: Integer[] ): Integer[] { return ia; }
function m21 ( ia: Integer[] ): String[] { var sary:String[]; for(i in ia) {insert i.toString() into sary;} return sary; }
function m22 ( sa: String[]  ): String[] { return sa; }
function m3  ( i : String   ): Integer{ return java.lang.Integer.valueOf(i); }
function m4  ( ia: Integer[] ): Integer[] { return ia; }
function m5  ( s : String) { System.out.println({s}); }
function m6  ( i : Integer, s : String) : String { return "{s}{i}"; }
function m7  ( i : Integer, s : String) : String { return " {i.toString()}{s}"; }
function m8  ( i : Integer, s : String) : String {return s + i.toString(); }

//basic function calls
m();
m0(1);
m02("hello");
m02( "{m11(5).toString()}");
System.out.print( "{m12( "{m11(5).toString()}")} - "); //prints "5 - "
System.out.print( "{m6(2,"U")} - ");          //prints "U2 - "
System.out.print( "{m8(2,"U")} - ");          //prints "U2 - "
//several ways to nest functions as parameters
System.out.print( "{ m3(m11(m1(55)))} -");    //prints "55 - "
System.out.println( "{m1( m3(m11(m1(55))))}");  //prints "55 - "
System.out.print( m2([{m1( m3(m11(m1(1))))},{m1( m3(m11(m1(2))))},{m1( m3(m11(m1(3))))}])); //prints [ 1, 2, 3 ]
System.out.println( m21(m2([{m1( m3(m11(m1(1))))},{m1( m3(m11(m1(2))))},{m1( m3(m11(m1(3))))}]))); //prints [ 1, 2, 3 ]
m5( m6(101,"Hwy ") + m7(99," dead baboons ") + m8(8,"  That's a prime"));
System.out.println();

/*Functions are first-class objects (they can, for example,
  be assigned to variables, or */
System.out.println("Functions are first-class objects (they can, for example, be assigned to variables");
System.out.println( " N    N*N");
for(x in [2..12]) {
	var xx = sq(x);
System.out.println(" {x}   {xx}")
}
System.out.println("- - - - - - - - - - - - -");

/*passed as parameters to other functions.)*/
System.out.println("Functions may be passed as parameters to other functions");
System.out.println( " N    N*N");
for(x in [10..30 step 5]) {
	var xx = formatNumber( sq(x) );
	System.out.println(" {x}   {xx}")
}
System.out.println("- - - - - - - - - - - - -");

/*Functions may be anonymous:*/
System.out.println("Functions can be anonymous");
var mult = function(a:Number, b:Number):Number { a * b; }
for(a1 in [ 1..10], a2 in [ 1..10]){
System.out.println("{a1} x {a2} = {formatNumber( mult(a1, a2) )}");
}
System.out.println("- - - - - - - - - - - - -");


//recursive function to return fibonacci numbers
function fib(n:Integer):Integer {
	if (n<2) {return 1;}
	else {return (fib(n-1) + fib(n-2));}
}

//descending sequence lends itself to inverting a sequence
function flipSequence(iseq:Integer[]):Integer[] {
	  var newseq:Integer[];
	  for(i in [sizeof iseq-1 .. 0 step -1]) { insert iseq[i] into newseq; }
	  return newseq;
}

//sample Integer stack
public class TestIntStack {
  attribute thestack:Integer[]
	  	   on replace oldValue[indx  .. lastIndex]=newElements { }//System.out.println("replaced {String.valueOf(oldValue)}[{indx}..{lastIndex}] by {String.valueOf(newElements)}")};
  function getTop():Integer { var t = sizeof thestack-1; return t; }
  function push(s:Integer)    { insert s into thestack; }
  function push(ss:Integer[]) { insert ss into thestack;}
  function flip(){ thestack = reverse thestack; }
  public function pop():Integer  {
    var retval = thestack[sizeof thestack-1];
	 delete thestack[ sizeof thestack-1 ];
    return retval;
  }

  //find index of first Integer==s
  public function search( s:Integer ):Integer  {
		var pos = -1;
		for( idx in [sizeof thestack-1 .. 0 step -1 ]){
			if(thestack[idx]==s) {	pos = idx;	return pos;	}
		}
    return pos;
  }
  public function print() {		System.out.println(thestack);  }
}

function check( msg:String,f:Integer, ExpectedValue:Integer) {
	if(f <> ExpectedValue) { System.out.println("FAILED: {msg}; {f} != {ExpectedValue}"); }
}

System.out.println("a simple Stack application");
var tis: TestIntStack = new TestIntStack;
//regular object as arg
tis.push(5);
tis.push(8);
tis.push(22);
tis.push(11);
tis.push(95);
tis.print();
//sequence as arg
tis.push( [10,20,30]);
tis.print();
for(i in [1..4]) System.out.println("pop: {tis.pop()}");
tis.print();
// function returning sequence as arg
System.out.println("descending sequence lends itself to inverting a sequence, push flip of 5,10,15");
tis.push(flipSequence([ 5,10,15]));
tis.print();
check("tis.search(22)", tis.search(22),2);
System.out.println("...flip the stack with 'reverse'..");
tis.flip();
tis.print();
System.out.println("Recursive functions MUST have a return value specified.");
for(i in [0..20])System.out.print(" {fib(i)}");
System.out.println();
System.out.println("- - - - - - - - - - - - -");

