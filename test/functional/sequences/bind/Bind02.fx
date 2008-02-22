/**
 * Functional test: Binding a sequence
 * @test
 * @run
 */


import java.lang.*;

var UOE:Class = Class.forName("com.sun.javafx.runtime.BindingException");
var a:Integer[] = bind [12,234,45,9];
class Data {
	static attribute pass =0;
	static attribute fail =0;
	static attribute a = bind [12,234,45,9];
	static attribute b = bind { for(k in [1..10] where (k%2 == 0)) k };
	static attribute c:Integer[] = bind [];
	static attribute d:Integer[] = bind [a,b,c];
	static attribute e:Integer[] = bind [a,[b,d],c];
	static attribute f:Integer[] = bind [0..<9];
	static attribute g:Integer[] = bind f[4..];
	static attribute h:Integer[] = bind [0..9];
	static attribute i:Integer[] = bind g[0..<];
}
function check(k:Class, f:function()) {
	try { f(); Data.fail++; System.out.println("Expected {k.getName()} not thrown"); }
        catch (t:Throwable) {
	        if (k.isAssignableFrom(t.getClass())) { Data.pass++;}
                else { Data.fail++; t.printStackTrace();}
        }
};
System.out.println(Data.a); //[ 12, 234, 45, 9 ]
check(UOE,  function() { delete Data.a;});
check(UOE,  function() { delete 0 from Data.a;});
check(UOE,  function() { delete Data.a[-1];});
check(UOE,  function() { delete Data.a[0..Integer.MAX_VALUE];});
check(UOE,  function() { Data.a[-1] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[0] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.a-1)/2] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.a-1)] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[200] = 0; var dummy;}); // Bug 693
check(UOE,  function() { insert 23 into Data.a;});
check(UOE,  function() { insert 23 before Data.a[-1];});
check(UOE,  function() { insert 23 before Data.a[0];});
check(UOE,  function() { insert 23 before Data.a[100];});
check(UOE,  function() { insert 23 after Data.a[-1];});
check(UOE,  function() { insert 23 after Data.a[0];});
check(UOE,  function() { insert 23 after Data.a[100];});
System.out.println(Data.b); //[ 2, 4, 6, 8, 10 ]
check(UOE,  function() { delete Data.b;});
check(UOE,  function() { delete 0 from Data.b;});
check(UOE,  function() { delete Data.b[-1];});
check(UOE,  function() { delete Data.b[0..Integer.MAX_VALUE];});
check(UOE,  function() { Data.b[-2] = 9; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.b-1)/2] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.b-1)] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.b[4] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.b[200] = 0; var dummy;}); // Bug 693
check(UOE,  function() { insert 23 into Data.b;});
check(UOE,  function() { insert 23 before Data.b[-1];});
check(UOE,  function() { insert 23 before Data.b[0];});
check(UOE,  function() { insert 23 before Data.b[20];});
check(UOE,  function() { insert 23 after Data.b[-1];});
check(UOE,  function() { insert 23 after Data.b[0];});
check(UOE,  function() { insert 23 after Data.b[20];});
System.out.println(Data.c);//[ ]
check(UOE,  function() { delete Data.c;});
check(UOE,  function() { delete 0 from Data.c;});
check(UOE,  function() { delete Data.c[-1];});
check(UOE,  function() { delete Data.c[0..Integer.MAX_VALUE];});
check(UOE,  function() { Data.c[-2] = 9; var dummy;}); // Bug 693
check(UOE,  function() { Data.c[0] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.c[200] = 0; var dummy;}); // Bug 693
check(UOE,  function() { insert 23 into Data.c;});
check(UOE,  function() { insert 23 before Data.c[-1];});
check(UOE,  function() { insert 23 before Data.c[0];});
check(UOE,  function() { insert 23 after Data.c[-1];});
check(UOE,  function() { insert 23 after Data.c[0];});
System.out.println(Data.d);//[ 12, 234, 45, 9, 2, 4, 6, 8, 10 ]
check(UOE,  function() { delete Data.d;});
check(UOE,  function() { delete 0 from Data.d;});
check(UOE,  function() { delete Data.d[-1];});
check(UOE,  function() { delete Data.d[0..Integer.MAX_VALUE];});
check(UOE,  function() { Data.d[-1] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.d[0] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.d-1)/2] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.d-1)] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.d[200] = 0; var dummy;}); // Bug 693
check(UOE,  function() { insert 23 into Data.d;});
check(UOE,  function() { insert 23 before Data.d[-1];});
check(UOE,  function() { insert 23 before Data.d[0];});
check(UOE,  function() { insert 23 before Data.d[20];});
check(UOE,  function() { insert 23 after Data.d[-1];});
check(UOE,  function() { insert 23 after Data.d[0];});
check(UOE,  function() { insert 23 after Data.d[20];});
System.out.println(Data.e);//[ 12, 234, 45, 9, 2, 4, 6, 8, 10, 12, 234, 45, 9, 2, 4, 6, 8, 10 ]
check(UOE,  function() { delete Data.e;});
check(UOE,  function() { delete 0 from Data.e;});
check(UOE,  function() { delete Data.e[-1];});
check(UOE,  function() { delete Data.e[0..Integer.MAX_VALUE];});
check(UOE,  function() { Data.e[-1] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.e[0] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.e-1)/2] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.e-1)] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.e[200] = 0; var dummy;}); // Bug 693
check(UOE,  function() { insert 23 into Data.e;});
check(UOE,  function() { insert 23 before Data.e[-1];});
check(UOE,  function() { insert 23 before Data.e[0];});
check(UOE,  function() { insert 23 before Data.e[20];});
check(UOE,  function() { insert 23 after Data.e[-1];});
check(UOE,  function() { insert 23 after Data.e[0];});
check(UOE,  function() { insert 23 after Data.e[20];});
System.out.println(Data.f);//[ 0, 1, 2, 3, 4, 5, 6, 7, 8 ]
check(UOE,  function() { delete Data.f;});
check(UOE,  function() { delete 0 from Data.f;});
check(UOE,  function() { delete Data.f[-1];});
check(UOE,  function() { delete Data.f[0..Integer.MAX_VALUE];});
check(UOE,  function() { Data.f[-1] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.f[0] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.f-1)/2] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.f-1)] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.f[200] = 0; var dummy;}); // Bug 693
check(UOE,  function() { insert 23 into Data.f;});
check(UOE,  function() { insert 23 before Data.f[-1];});
check(UOE,  function() { insert 23 before Data.f[0];});
check(UOE,  function() { insert 23 before Data.f[20];});
check(UOE,  function() { insert 23 after Data.f[-1];});
check(UOE,  function() { insert 23 after Data.f[0];});
check(UOE,  function() { insert 23 after Data.f[20];});
System.out.println(Data.g);//[ 4, 5, 6, 7, 8 ]
check(UOE,  function() { delete Data.g;});
check(UOE,  function() { delete 0 from Data.g;});
check(UOE,  function() { delete Data.g[-1];});
check(UOE,  function() { delete Data.g[0..Integer.MAX_VALUE];});
check(UOE,  function() { Data.g[-1] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.g[0] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.g-1)/2] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.g-1)] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.g[200] = 0; var dummy;}); // Bug 693
check(UOE,  function() { insert 23 into Data.g;});
check(UOE,  function() { insert 23 before Data.g[-1];});
check(UOE,  function() { insert 23 before Data.g[0];});
check(UOE,  function() { insert 23 before Data.g[20];});
check(UOE,  function() { insert 23 after Data.g[-1];});
check(UOE,  function() { insert 23 after Data.g[0];});
check(UOE,  function() { insert 23 after Data.g[20];});
System.out.println(Data.h);//[ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ]
check(UOE,  function() { delete Data.h;});
check(UOE,  function() { delete 0 from Data.h;});
check(UOE,  function() { delete Data.h[-1];});
check(UOE,  function() { delete Data.h[0..Integer.MAX_VALUE];});
check(UOE,  function() { Data.h[-1] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.h[0] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.h-1)/2] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.h-1)] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.h[200] = 0; var dummy;}); // Bug 693
check(UOE,  function() { insert 23 into Data.h;});
check(UOE,  function() { insert 23 before Data.h[-1];});
check(UOE,  function() { insert 23 before Data.h[0];});
check(UOE,  function() { insert 23 before Data.h[20];});
check(UOE,  function() { insert 23 after Data.h[-1];});
check(UOE,  function() { insert 23 after Data.h[0];});
check(UOE,  function() { insert 23 after Data.h[20];});
System.out.println(Data.i);//[ 4, 5, 6, 7 ]
check(UOE,  function() { delete Data.i;});
check(UOE,  function() { delete 0 from Data.i;});
check(UOE,  function() { delete Data.i[-1];});
check(UOE,  function() { delete Data.i[0..Integer.MAX_VALUE];});
check(UOE,  function() { Data.i[-1] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.i[0] = 0; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.i-1)/2] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.a[(sizeof Data.i-1)] = Integer.MIN_VALUE; var dummy;}); // Bug 693
check(UOE,  function() { Data.i[200] = 0; var dummy;}); // Bug 693
check(UOE,  function() { insert 23 into Data.i;});
check(UOE,  function() { insert 23 before Data.i[-1];});
check(UOE,  function() { insert 23 before Data.i[0];});
check(UOE,  function() { insert 23 before Data.i[20];});
check(UOE,  function() { insert 23 after Data.i[-1];});
check(UOE,  function() { insert 23 after Data.i[0];});
check(UOE,  function() { insert 23 after Data.i[20];});

System.out.println("Pass count: {Data.pass}");
if(Data.fail > 0){ throw new Exception("Test failed"); }
