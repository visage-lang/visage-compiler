/*
 * Unlike the Java programming language, JavaFX script is an expression language. 
 * All executable statements are expressions which consist of zero or more inputs and a single output. 
 * This includes conditionals, loops, and even blocks.
 */
import java.lang.System;
import java.lang.Math;
import java.lang.Exception;

var pass = 0;
var fail = 0;
function print(msg:String) { System.out.println(msg); }
function check(msg:String) { pass++; }
function checkS(s1:String, s2:String,msg:String) { if(s1==s2){pass++; }else {fail++;print("FAILED: {msg} : {s1} != {s2}");} }
function checkI(i1:Integer, i2:Integer,msg:String) { if(i1==i2){pass++; }else {fail++;print("FAILED: {msg} : {i1} != {i2}");} }
function checkIs(i1s:Integer[], i2s:Integer[],msg:String) { if(i1s==i2s){pass++; }else {fail++;print("FAILED: {msg} : {i1s} != {i2s}");} }
function checkSs(s1s:String[], s2s:String[],msg:String) { if(s1s==s2s){pass++; }else {fail++;print("FAILED: {msg} : {s1s} != {s2s}");} }
function checkB(b:Boolean,msg:String) { if(b){pass++;} else{fail++;print("FAILED: {msg}");}}
function checknotB(b:Boolean,msg:String) { if(b){fail++;print("FAILED: {msg}");}else{pass++;} }
function report() { System.out.println("tests: {pass + fail}  passed: {pass}  failed: {fail}"); }
var rand = (Math.random() * 100).intValue();
var s:String = null;
if (rand % 2 == 0) {
     s = "rand is even";
    checkS(s,"rand is even","Check random number");
} else {
     s = "rand is odd";
     checkS(s,"rand is odd","Check random number");
}

/*
 * In the above example the then and else clauses of the conditional "if" are expressions in their own right, 
 * namely block expressions [to-do, link to block expressions section below]
 */

/*
 * Logical Expressions

This section lists and defines the logical expressions available in JavaFX� Script.
___if Expression___

The if expression is like the expression in the Java� programming language, except that curly braces are always required around the then and else clauses, unless the the else clause is another if expression.

Example:
*/
var condition1 = true;
var condition2 = false;
function logicalexpr(condition:Boolean,desc:String) {
       if (condition) {
           checkS(desc,"condition is true","check logical condition: true");  
       } else if (not condition) {
           checkS(desc,"condition is false","check logical condition: false");  
       } else {
           System.out.println("not Condition 1 or Condition 2...should never get here.");
       }
}
logicalexpr(condition1,"condition is true");
logicalexpr(condition2,"condition is false");

/* ___While Statement___

The JavaFX Script while statement is like the statement in the Java Programming language, 
except that curly braces are always required around the body.

Example:
*/
       var i = 0;
       var seqi:Integer[];
       while (i < 10) {
           if (i > 5) { break; }
           insert i into seqi;
           i += 1;
       }
       checkIs(seqi,[0,1,2,3,4,5],"check while statement {seqi} != {[0,1,2,3,4,5]}");

/*
___try Statement___

The JavaFX Script try statement is like the statement in the Java Programming language, but with JavaFX Script variable declaration syntax.
Note
In JavaFX Script any object can be thrown and caught, not just those that extend java.lang.Throwable.

Example:
*/
       try {	
          //throw "Hello";	
	  throw new Exception("Exception thrown");
       } catch (e:Exception) {
		    checkS(e.getMessage(),"Exception thrown","check catch of thrown Exception");
       } catch (any) {
          System.out.println("caught something not a String: {any}"); 
       } finally {
		    check("finally reached, should increment pass count");
       }

/*
___for Statement___

QUESTION: We need a new intro to go with the 'for' statement.

The header of the JavaFX Script for statement uses the same syntax as the foreach list comprehension operator. 
However; in this case the body of the statement is executed for each element generated by the list comprehension.

Examples:
*/
var seq2:Integer[];
   for (j in [0..10]) {		insert j into seq2;        }
	checkIs( seq2,[0,1,2,3,4,5,6,7,8,9,10],"check basic foreach iteration");

   // print only the even numbers using a filter
	seq2=[];
   for (k in [0..10] where k % 2 == 0) {	insert k into seq2;        }
	checkIs( seq2,[0,2,4,6,8,10],"check list comprehension with modulo 2");

   // print only the odd numbers using a range expression
	seq2=[];
   for (l in [1..10 step 2]) {  insert l into seq2;  }
	checkIs(seq2,[1,3,5,7,9],"check list comprehension with step 2");

   // print the cartesian product
	var cartProds:String[];
	var cartProdCount = 0;
   for (m in [0..10], n in [0..10]) {
		cartProdCount++;
		insert "({m},{n})" into cartProds;
   }
  var expectedarray=[ "(0,0)", "(0,1)", "(0,2)", "(0,3)", "(0,4)", "(0,5)", "(0,6)", "(0,7)", "(0,8)", "(0,9)", "(0,10)",
  "(1,0)", "(1,1)", "(1,2)", "(1,3)", "(1,4)", "(1,5)", "(1,6)", "(1,7)", "(1,8)", "(1,9)", "(1,10)",
  "(2,0)", "(2,1)", "(2,2)", "(2,3)", "(2,4)", "(2,5)", "(2,6)", "(2,7)", "(2,8)", "(2,9)", "(2,10)",
  "(3,0)", "(3,1)", "(3,2)", "(3,3)", "(3,4)", "(3,5)", "(3,6)", "(3,7)", "(3,8)", "(3,9)", "(3,10)",
  "(4,0)", "(4,1)", "(4,2)", "(4,3)", "(4,4)", "(4,5)", "(4,6)", "(4,7)", "(4,8)", "(4,9)", "(4,10)",
  "(5,0)", "(5,1)", "(5,2)", "(5,3)", "(5,4)", "(5,5)", "(5,6)", "(5,7)", "(5,8)", "(5,9)", "(5,10)",
  "(6,0)", "(6,1)", "(6,2)", "(6,3)", "(6,4)", "(6,5)", "(6,6)", "(6,7)", "(6,8)", "(6,9)", "(6,10)",
  "(7,0)", "(7,1)", "(7,2)", "(7,3)", "(7,4)", "(7,5)", "(7,6)", "(7,7)", "(7,8)", "(7,9)", "(7,10)",
  "(8,0)", "(8,1)", "(8,2)", "(8,3)", "(8,4)", "(8,5)", "(8,6)", "(8,7)", "(8,8)", "(8,9)", "(8,10)",
  "(9,0)", "(9,1)", "(9,2)", "(9,3)", "(9,4)", "(9,5)", "(9,6)", "(9,7)", "(9,8)", "(9,9)", "(9,10)",
  "(10,0)","(10,1)","(10,2)", "(10,3)", "(10,4)", "(10,5)", "(10,6)", "(10,7)", "(10,8)", "(10,9)", "(10,10)" ];
checkI(cartProdCount, 121,"check 2 dimensional foreach count");
checkSs(cartProds,expectedarray,"multiple expression for loop");

/*
___return Statement___
The JavaFX Script return statement is like the statement in the Java Programming language:
Example:
*/
       function add(x, y) {
           return x + y;
       }
/*
___throw Statement___

The JavaFX Script throw statement is like the statement in the Java Programming language. However; any object can be thrown, not just those that extend java.lang.Throwable.

Examples:
*/

       function fooE() {
           throw new Exception("this is a java exception");
       }

       function barE() {
           //throw "just a string";
       }
/*
__break and continue Statements___

The JavaFX Script break and continue statements are like the statement in the Java Programming language; however labels are not supported. As in Java, break and continue must appear inside the body of a while or for statement.

Examples:
*/
       function foo() {
		     var seq:Integer[];
          for (i in [0..10]) 
			 {
              if (i > 5) {  break; }
              if (i % 2 == 0) { continue;  }
              insert i into seq;
          }
          checkIs( seq,[1,3,5],"check break and continue in for loop");
       }
		 foo();

       function bar() {
           var i = 0;
			  var seq:Integer[];
           while (i < 10) 
			  {  
               if (i > 5)      { break;     } //step when i gets larger than 5
               if (i % 2 == 0) { i++; continue;  } //goto to top of while i is even, ie. go on if i is odd
					insert i into seq;
               i += 1;
           }
			  checkIs(seq,[1,3,5],"check break and continue in while loop");
       }
		 bar();
/*
___Block Expressions___

A block expression consists of a list of statements (which can be declarations or expressions) surrounded by curly braces and separated by semicolons. If the last statement is an expression, then the value of a block expression is the value of the last expression; otherwise the block expression has void type.

So, the above example from the top of this section could also be written as follows:
*/

rand = (Math.random() * 100).intValue();
s = if (rand % 2 == 0) {
     "rand is even";
} else {
     "rand is odd";
};
if (s == "rand is even") { checkS(s,"rand is even", "check block expression using Math.random"); }
else {checkS(s,"rand is odd", "check block expression using Math.random");}
//System.out.println(s);

/*
 * Alternatively the braces can be omitted:
 */

rand = (Math.random() * 100).intValue();
s = if (rand % 2 == 0) "rand is even" else "rand is odd";
if (s == "rand is even") { checkS(s,"rand is even", "check block expression using Math.random"); }
else {checkS(s,"rand is odd", "check block expression using Math.random");}

/*
The Java programming language contains both an "if" statement, and a conditional expression, e.g, a < b ? a : b.

Thanks to block expressions, the JavaFX "if" expression takes the place of both.
Range Expressions

As mentioned in chapter two [TO-DO: confirm that location and provide link] it is possible to define a sequence of numeric values forming an arithmetic series using the following syntax:

[number1..number2]

Such an expression defines an sequence whose elements consist of the integers from number1 to number2 (inclusive).

Example:
*/
var nums = [0..3];
checkIs(nums,[0,1,2,3],"declaration of sequence"); // prints true
/*
By default the interval between the values is 1 but 
it's also possible to specify a different interval by including the next number in the sequence after number1 separated by a comma. 
For example, the following expression defines an sequence consisting of the odd numbers between 1 and 10:

[1,3..10]

If number1 is greater than number2 a descending series is created:
*/

/* This is the interpretted style of declaration. */
nums = [3..0];
checknotB(nums==[3,2,1,0],"check descending sequence declare with interpreter style declaration."); // not true
/* "new" way is to specify step increment, but... */
nums = [3..0 step 1];
checknotB(nums==[3,2,1,0],"check descending sequence declared with positive step increment"); // not true
/* for descending sequences, step must be negative. */
nums = [3..0 step -1];
checkIs(nums,[3,2,1,0],"check descending sequence");  //this should work!

/* 
NOTES:

1. step seqences must be specified with the 'step' keyword
2. opersation if replaced by function
3. functions should have a return value specified if it returns
4. use of 'foreach' needs to be updated. I called it a "foreach" syntax
*/

report();
