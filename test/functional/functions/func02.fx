import java.lang.System;
import java.lang.Exception;

/**
 * Functional test: Functions in sequence
 * @test
 * @run
 */


var pass=0;
var fail=0;
function equal(f:function(:Integer,:Integer):Integer,
		one:Integer,two:Integer, v:Integer):Boolean {
		f(one,two) ==v;	
}
var add = function(a:Integer, b:Integer):Integer {
	a+b
}
var mul = function(a:Integer, b:Integer):Integer {
	a*b
}
var div = function(a:Integer, b:Integer):Integer {
	a/b
}
var sub = function(a:Integer, b:Integer):Integer {
	a-b
}

//var funseq1=[add]//JFXC 852
var funseq2 = [add,sub];
var funseq3 = [funseq2,mul];
var funseq4 = [funseq3,div];
var input = [4,2];
var result = [6,2,8,2];
//var call = function(funseq) { //JFXC853
//	for(i in funseq) {
//		var f = i as function(:Integer, :Integer):Integer;
//		if(equal(f,input[0],input[1],result[indexof i])) pass++
//		else fail++;
//	}
//}
for(i in funseq2) {
	var f = i as function(:Integer, :Integer):Integer;
	if(equal(f,input[0],input[1],result[indexof i])) pass++
	else fail++;
}
for(i in funseq3) {
	var f = i as function(:Integer, :Integer):Integer;
	if(equal(f,input[0],input[1],result[indexof i])) pass++
	else fail++;
}
for(i in funseq4) {
	var f = i as function(:Integer, :Integer):Integer;
	if(equal(f,input[0],input[1],result[indexof i])) pass++
	else fail++;
}
System.out.println("Pass count: {pass}");
if(fail > 0){ throw new Exception("Test failed"); }
