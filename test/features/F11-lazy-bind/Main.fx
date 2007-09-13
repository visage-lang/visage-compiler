/* Feature test #11 -- lazy attribute binding
 * Demonstrates: lazy binding
 * @test
 * @run
 */

import java.lang.System;

class Test {
	attribute a : String;
	attribute b : String;
	attribute lz : String = bind lazy lzdo(a, b);
	attribute lzcount : Integer = 0;
	operation lzdo(a : String, b : String) {
		lzcount = lzcount + 1;
		"({a}, {b})"
	}
	attribute eg : String = bind egdo(a, b);
	attribute egcount : Integer = 0;
	operation egdo(a : String, b : String) {
		egcount = egcount + 1;
		"({a}, {b})"
	}
}

var t = Test { 
	a: "ink"
	b: "blot"
};

t.a = "pink";
t.b = "ribbon";
t.a = "pasta";
t.b = "primavera";

System.out.println("{t.lz} == {t.eg}");
System.out.println("Lazy:  {t.lzcount}");
System.out.println("Eager:  {t.egcount}");
