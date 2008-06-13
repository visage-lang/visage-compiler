import java.lang.System;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * @test
// * @compilefirst node.fx
// * @compilefirst linklist.fx
 * @run
 *
 * Simple performance test of adding and then iterating 1,000,000 nodes in a linked list.
 *
 * Running with javac/java results in about 1100ms to add adn 47ms to iterate on first run. 
 * Subsequent runs will see the add time cut in half or better while the iterate times remain 
 * about the same.  
 *
 * This is run thrice using the java.util.LinkedList and using the simple linklist one written below.
 * Oddly it seems running in fx using java.util.LinkedList appears faster than javac/java code.
 * I'm not sure why the difference is 2:1(LinkedList over linklist) or if the difference is due to 
 * one collecttion being written in java and the other in fx...or I wrote a lousy linklist. :)
 *
 * JavaFX gets roughly 1500-1700ms to add and 125 - 150ms to iterate. 
 * Subsequent runs don't seem to impact the times, especially iteration.
 *
 * I set the threshold high to allow for machine differences.
 *
 * TODO: set acceptable threshold
 */

var JaLINK_ADD_CUMM = 0;
var JaLINK_ITR_CUMM = 0;
var jli = 0;

var FxLINK_ADD_CUMM = 0;
var FxLINK_ITR_CUMM = 0;
var fli = 0;

var jLINK_ADD:Integer=650;
var jLINK_ITR:Integer=60;
var fLINK_ADD:Integer=1250;
var fLINK_ITR:Integer=100;

var bDebugOut = false;
function DebugOut(msg:String) { if(bDebugOut) System.out.print(msg); }
function DebugOutln(msg:String) { if(bDebugOut) System.out.println(msg); }

function checkresults() {
   if(JaLINK_ADD_CUMM/jli <= jLINK_ADD and JaLINK_ITR_CUMM/jli <=jLINK_ITR ) {
	   DebugOutln("Java LinkedList: Average Time to add: {JaLINK_ADD_CUMM/jli}(ms) compare to {jLINK_ADD}ms;  Time to iterate: {JaLINK_ITR_CUMM/jli}ms compare to {jLINK_ITR}ms");
		System.out.println("PASS");		
		}
	else {System.out.println("FAIL, times appear to be too slow for java LinkedList.");
		   System.out.println("Average Time to add: {JaLINK_ADD_CUMM/jli}(ms) compare to {jLINK_ADD}ms;  Time to iterate: {JaLINK_ITR_CUMM/jli}ms compare to {jLINK_ITR}ms");
	}

	
	if(FxLINK_ADD_CUMM/fli <= fLINK_ADD and FxLINK_ITR_CUMM/fli <= fLINK_ITR ) {
		DebugOutln("FX link list: Time to add: {FxLINK_ADD_CUMM/fli}(ms) compare to {fLINK_ADD}ms;  Time to iterate: {FxLINK_ITR_CUMM/fli}ms compare to {fLINK_ITR}ms");
		System.out.println("PASS");		
	}
	else {System.out.println("FAIL, times appear to be too slow.");
		   System.out.println("Time to add: {FxLINK_ADD_CUMM/fli}(ms) compare to {fLINK_ADD}ms;  Time to iterate: {FxLINK_ITR_CUMM/fli}ms compare to {fLINK_ITR}ms");
	}
}

function osInfo()   {
    var javaversion:String = System.getProperty("java.version");
    var osInfo = System.getProperty("os.name");
    var osver = System.getProperty("os.version");
	 osInfo = "{osInfo} v{osver}";
    System.out.println("java version: {javaversion}");
    System.out.println("OS version  : {osInfo}");
    System.out.println();
}

function runtest(iterations:Integer, f:function():Integer) {
	for ( i in [ 1..iterations]) { DebugOut("{i}. "); f(); }
};

/**
 * This uses java.util.LinkedList
 * It's much faster. 
 */
function test0() {
DebugOut("test0: ");
		//LinkedList
    var st:Number = System.currentTimeMillis();
      var xs:LinkedList = new LinkedList();
      for (i in [ 0..500000]) {      	xs.add(i);      }
    var st2:Number = System.currentTimeMillis();
    var val = (st2-st);
	 JaLINK_ADD_CUMM += val as Integer;
DebugOut("Linked List0: Time to add="+val+" (msec)");

      var it:Iterator = xs.iterator();
	 st = System.currentTimeMillis();
      while (it.hasNext())  {      	var ival:Integer = it.next() as Integer;      }
    st2 = System.currentTimeMillis();
    val = (st2-st);
DebugOutln("   Time to iterate="+val+" (msec)");
    JaLINK_ITR_CUMM += val as Integer;
	  jli++;
}

/**
 * This uses included linkedlist and is much slower than built-in java.util.LinkedList
 * Why?
 */
function test1() {
DebugOut("test1: ");
var l = new linklist;
//set timer for adding nodes
var st:Number = System.currentTimeMillis();
	// add 1,000,000 nodes
	for( i in [ 1..500000]) {l.add(i);}
var st2:Number = System.currentTimeMillis();
var val = (st2 -st);
DebugOut("Linked List1: Time to add="+val+" (msec)");
FxLINK_ADD_CUMM += val as Integer;

// Iterate the nodes
	var iter:node = l.head;
st = System.currentTimeMillis();
	while (iter.next <> null) { iter  = iter.next;}
st2 = System.currentTimeMillis();
val = (st2 -st);
DebugOutln("   Time to iterate="+val+" (msec)");
FxLINK_ITR_CUMM += val as Integer;
fli++;
//System.out.println("sizeof list: {l.size}");
//System.out.println("Last node's data: {l.end.data}");
}

/** Integer node and a linked list of nodes */
public class node {
 attribute data = 0;
 attribute prev:node = null;
 attribute next:node = null;
}

public class linklist {
	 attribute head:node = new node;
	 attribute tail:node = head;
	 attribute end:node = head;
	 attribute mrnodes = false;
	 attribute size = 0;
	 // add a node
	 function add(newnode:node) {
		 if(head.next == null) {head.next=newnode; newnode.prev=head; tail=newnode;}
		 else {  tail.next = newnode; newnode.prev=tail; tail=newnode; }
		 size++;	end=tail;
	 }
	 // add node by adding Integer
	 function add(newdata:Integer) {
		var newnode = node { data : newdata; }
		if(mrnodes) {  tail.next = newnode; newnode.prev=tail; tail=newnode; }
		else {head.next=newnode; newnode.prev=head; tail=newnode; mrnodes=true;}
		size++;	end=tail;
	 }
}
//*/
if(bDebugOut) osInfo();
runtest(6, test1);
runtest(6, test0);
checkresults();
















