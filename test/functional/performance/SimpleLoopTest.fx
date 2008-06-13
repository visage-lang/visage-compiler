import java.lang.System;
/**  
 * @test
 * @run
 *
 * SimpleLoopTest for if statements to test optimization of redundant if's.
 * 1 million iterations shows redundant if's are not optimized
 * You can see this by disassembling the code (with javap -c). 
 * test1() has multiple if's while it could be optimized to be as test2().
 *
 * For now, test accepts a difference of 35ms between existing and optimized loops. 
 * Test iterates 6 times and only need to produce 1 acceptable measurement to pass.
 */

var bControl:Boolean = true;
/** Some runs produce less than expected result so pass/fail is based on several iterations.
 *  Any one run that produces acceptable numbers results in PASS result for test. To fail, all
 *  iterations must result in less than accceptable numbers.
 *  Some run do far better than this number. To see full output, change DEBUG to true.
 */  var AcceptableDifference = 35;
var DiffQueue:Integer[];
var ExpectedOptimizedDifference = 6;
var ITERATIONS = 6;
var DEBUG=false;
var TEST_IS_ACCEPTABLE = false;
function debugOut(msg:String) { if(DEBUG){System.out.print(msg);}}
function sDebugOut(msg:Integer[]) { if(DEBUG){System.out.print(msg);}}
function debugOutln(msg:String) { if(DEBUG){System.out.println(msg);}}
function check(diff:Integer, threshhold:Integer) {
   insert diff into DiffQueue;
	if (diff > threshhold) {debugOutln("WARNING: Difference - {diff} -  exceeds threshhold - {threshhold}");}
	 else { TEST_IS_ACCEPTABLE=true;} 
	if( diff < ExpectedOptimizedDifference) { debugOutln("NOTE: Performance diff of {diff}ms far exceeds current threshhold of {ExpectedOptimizedDifference}ms. Test needs updating to optimized threshhold."); }
 }
 function AvgDiff( seq:Integer[] ):Integer {
	var sum = 0;
	for(d in seq ) sum +=d;
	var avg = sum/(sizeof seq);
	debugOut("average diff for "); sDebugOut(seq); debugOutln(" = {avg}ms");
	return avg;
}

function results() { if (TEST_IS_ACCEPTABLE) {System.out.println("PASS: At least one iteration resulted in acceptable performance."); }
                     else { System.out.println("FAILED: All {ITERATIONS} results in times exceed acceptable threshhold."); 
							       System.out.print("Difference Queue:"); System.out.println(DiffQueue);} }
var a:Integer = 0;
var b:Integer = 0;
var c:Integer = 0;
var d:Integer = 0;
var e:Integer = 0;
var f:Integer = 0;

function test1() {
	if(bControl) a = 1;
	if(bControl) b = 2;
	if(bControl) 
		if(bControl) 
			if(bControl) 
				if(bControl)	c=3;
	if(bControl)	d=4;
	if(bControl)	e=5;
}
 a=b=c=d=e=0;

function test2() {
	if(bControl) {
	a=1;
	b=2;
	c=3;
	d=4;
	e=5;
	}
}

function test() {
var st:Number = 0;
var st2:Number = 0;
var val1:Number =99;
var val2:Number =0;
//*
st = System.currentTimeMillis();
// add 1,000,000 nodes
for( i in [ 1..1000000]) {	test1()}
st2 = System.currentTimeMillis();
val1 = (st2 -st);
debugOutln("Badly written code took {val1}(ms)");
debugOutln("- - - - - - - - - - - - - - - -");
//*/
st = System.currentTimeMillis();
// add 1,000,000 nodes
for( i in [ 1..1000000]) {	test2()  }
st2 = System.currentTimeMillis();
val2 = (st2 -st);
debugOutln("More optimized code took {val2}(ms)");
check(val1-val2,AcceptableDifference);
val1 = 99; val2=0;
debugOutln("==============================");
//*/
}

for(i in [ 0..ITERATIONS]) {  test(); }

AvgDiff( DiffQueue );
results();
