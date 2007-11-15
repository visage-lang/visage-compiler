import java.lang.System;

class B {
	attribute a : A;
	function op() : Void { a.x = 4; }
}

var b = B { a: new A };
b.op();
System.out.println(b.a.x);
