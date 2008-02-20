/**
 * Functional test: Binding a sequence
 * @test
 * @run
 */

import java.lang.*;

class Data {
	static attribute pass =0;
	static attribute fail =0;
}
function equal(x,y) {
	if(x==y){ Data.pass++;} 
	else { System.out.print(x); System.out.println(x); Data.fail++;};
}

var a = [2..6 step 2];
var b = [1..6 step 2];
var c = bind a + " " + b;
var d = a + " " + b;
var e = bind [a,b];
var e1 = bind [b,a];
var f = [a,b];
delete a;
delete b;
a= {for(i in [1..6] where i%2==0)i};
b= {for(i in [1..6] where i%2<>0)i};
equal(c,a + " " + b);
equal(c,d);
var g = bind for(i in [0..2], j in [1..2]) {(if(j<>1) e[i] else e1[i])}; 
equal(g,[1..<7 step 1]);
a = [-1..-3 step -1];
b = [e1,e1];
equal(g,[ 1, -1, 3, -2, 5, -3]);
delete a;
equal(e,b);
var h = bind [a,e1];
delete a;
equal(h,b);
a = [f,h];
delete b;
equal(a,[ 2, 4, 6, 1, 3, 5, 1, 3, 5, -1, -2, -3, 1, 3, 5, -1, -2, -3 ]); 
equal(sizeof b, 0);
equal(c,(a+" "+b));
equal(d,"[ 2, 4, 6 ] [ 1, 3, 5 ]");
equal(e,a);
equal(e1,a);
equal(g,[ 2, 2, 4, 4, 6, 6 ]);
equal(h,[e,e1]);

System.out.println("Pass count: {Data.pass}");
if(Data.fail > 0){ throw new Exception("Test failed"); }
