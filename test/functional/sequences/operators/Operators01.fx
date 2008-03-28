/*
 * Simple functional test for sequence operators.
 * Operators : sizeof, indexof, after, before, delete, reverse
 * @test
 * @run
 */


import java.lang.*;

var pass=0;
var fail=0;

// Different ways of creating sequence
var a = [1,2,3,4];
var b = { for(k in [1..10] where (k%2 == 0)) k };
var c:Integer[] = [];  
var d = [a,b,c];
var e = [a,[b,d],c];
var f = [0..<9];
var g = f[4..];
var h = [0..9];
var i = g[0..<];
var j = f[n|indexof n>2];

var seq = [a as Object, b as Object,
	   c as Object, d as Object,
	   e as Object, f as Object,
	   g as Object, h as Object,
	   i as Object, j as Object];

function checkAfter(sequence:Object[]) {
	for(flag in sequence) {
		var x = flag as Integer[];
		var y = [x, x];
		insert x after x[-1];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		insert x after x[-1];
		check(x==y);
		
		x = flag as Integer[];
		y = [x];
		insert x after x[-2];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		insert x after x[-2];
		check(x==y);
		
		x = flag as Integer[];
		y = [x];
		insert x after x[Integer.MAX_VALUE-100];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		insert x after x[Integer.MAX_VALUE-100];
		check(x==y);

		x = flag as Integer[];
		y = [x,x];
		insert x after x[(sizeof x) - 1];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		insert x after x[(sizeof x) - 1];
		check(x==y);
		
		x = flag as Integer[];
		y = [x];
		insert x after x[sizeof x];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		insert x after x[sizeof x];
		check(x==y);

	}
}
function checkBefore(sequence:Object[]) {
	for(flag in sequence) {
		var x = flag as Integer[];
		var y = [x, x];
		insert x before x[0];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		insert x before x[0];
		check(x==y);
		
		x = flag as Integer[];
		y = [x];
		insert x before x[-1];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		insert x before x[-1];
		check(x==y);
		
		x = flag as Integer[];
		y = [x];
		insert x before x[Integer.MAX_VALUE-100];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		insert x before x[Integer.MAX_VALUE-100];
		check(x==y);
		
		x = flag as Integer[];
		y = [x,x];
		insert x before x[sizeof x];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		insert x before x[sizeof x];
		check(x==y);
		
		x = flag as Integer[];
		y = [x];
		insert x before x[sizeof x + 1];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		insert x before x[sizeof x + 1];
		check(x==y);
	}
}

function checkDelete(sequence:Object[]) {
	for(flag in sequence) {
		var x = flag as Integer[];
		var y:Integer[] = [];
		delete x;
		check(x==y);
		x = reverse (flag as Integer[]);
		delete x;
		check(x==y);
		
		x = flag as Integer[];
		y = [x];
		delete x[-1];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		delete x[-1];
		check(x==y);
		
		x = flag as Integer[];
		y = [x];
		delete x[1000];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		delete x[1000];
		check(x==y);
		
		x = flag as Integer[];
		y = [x];
		delete x[sizeof x];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		delete x[sizeof x];
		check(x==y);
		
		x = flag as Integer[];
		y = [x];
		delete Integer.MAX_VALUE from x;
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		delete Integer.MAX_VALUE from x;
		check(x==y);
		
		x = flag as Integer[];
		y = [x,x];
		for(z in x) {
			delete z from y; 
		}
		check([]==y);
		x = reverse (flag as Integer[]);
		y = reverse y;	
		for(z in x) {
			delete z from y; 
		}
		check([]==y);
		
		x = flag as Integer[];
		y = [x,x];
		for(z in x) {
			delete y[0]; 
		}
		check(x==y);
		x = reverse (flag as Integer[]);
		y = [x,x];	
		for(z in x) {
			delete y[0]; 
		}
		check(x==y);
		
		x = flag as Integer[];
		y = [x];
		delete x[-100..-200];
		check(x==y);
		delete x[-100..<-200];
		check(x==y);
		delete x[-100..<];
		check(x==y);
		delete x[1000..2000];
		check(x==y);
		delete x[1000..<2000];
		check(x==y);
		delete x[1000..<];
		check(x==y);
		delete x[0..100];
		check(x==[]);
		x = reverse (flag as Integer[]);
		y = reverse y;
		delete x[-100..-200];
		check(x==y);
		delete x[-100..<-200];
		check(x==y);
		delete x[-100..<];
		check(x==y);
		delete x[1000..2000];
		check(x==y);
		delete x[1000..<2000];
		check(x==y);
		delete x[1000..<];
		check(x==y);
		delete x[0..100];
		check(x==[]);

		
		x = flag as Integer[];
		y = [x];
		delete x[-100..100];
		check(x==y);
		if(not (sizeof x == 0)) {
			delete x[0..<];
			check(sizeof x == 1);
		}
		x = reverse (flag as Integer[]);
		y = reverse y;
		delete x[-100..100];
		check(x==y);
		if(not (sizeof x == 0)) {
			delete x[0..<];
			check(sizeof x == 1);
		}

	}
}

function checkIndexOf(seq:Object[]) {
	for(flag in seq) {
		var x = flag as Integer[];
		var y:Integer[] = [];
		x = x[n|indexof n>sizeof x];
		check(x==y);
		x = reverse (flag as Integer[]);
		x = x[n|indexof n>sizeof x];
		check(x==y);
		
		x = flag as Integer[];
		x = x[n|indexof n>sizeof x-1];
		check(x==y);
		x = reverse (flag as Integer[]);
		x = x[n|indexof n>sizeof x-1];
		check(x==y);
		
		x = flag as Integer[];
		y = x;
		x = x[n|indexof n > Integer.MIN_VALUE];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		x = x[n|indexof n > Integer.MIN_VALUE];
		check(x==y);
		
		x = flag as Integer[];
		y = x;
		x = x[n|indexof n < Integer.MAX_VALUE];
		check(x==y);
		x = reverse (flag as Integer[]);
		y = reverse y;
		x = x[n|indexof n < Integer.MAX_VALUE];
		check(x==y);

		x = flag as Integer[];
		if(not (sizeof x ==0)) {
			x = x[n|indexof n>(sizeof x -2)];
			check(sizeof x == 1);
		
			x = reverse (flag as Integer[]);
			x = x[n|indexof n>sizeof x-2];
			check(sizeof x == 1);
		}
	}
}

function check(x:Boolean) {
	if(x) pass++ else fail++;
}

checkAfter(seq);
checkAfter(reverse seq);
checkBefore(seq);
checkBefore(reverse seq);
checkDelete(seq);
checkDelete(reverse seq);
checkIndexOf(seq);
checkIndexOf(reverse seq);

System.out.println("Pass count : {pass}");
if(fail > 0) {
	System.out.println("Fail count : {fail}");
	throw new Exception("Test failed"); 
}
