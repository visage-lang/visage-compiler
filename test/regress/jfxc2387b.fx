/*
 * Regression test for JFXC-2387 : reference to + is ambiguous, both method +(double,double) in and method +(int,int) in match
 *
 * @test
 * @run
 */

import java.util.*;
import java.lang.System;

/* note: some legal instanceof have been commented because of a bug in translation - the runtime
currently do not support instance test whose target type is a sequence type */

//reference types

class A {}
class B {}
class C extends A,B {}
var c = C{};
var b:B = c;
var a:A = c;
var x:ArrayList = new ArrayList();

println(a instanceof ArrayList);
//(a instanceof ArrayList[]);
//([a] instanceof ArrayList[]);

println(a instanceof List);
//(a instanceof List[]);
//([a] instanceof List[]);

println(a instanceof B);
println(b instanceof A);
//(b instanceof A[]);


//(a instanceof B[]);
//([a] instanceof B[]);
//(b instanceof A[]);

println(x instanceof Collection);
//(x instanceof Collection[]);
//([x] instanceof Collection[]);

//null

//(null instanceof Integer[]);

//(null instanceof A[]);
println(null instanceof A);

//([] instanceof A[]);


//primitives

//(1.0 instanceof Integer[]);
//(1 instanceof Number[]);
//([1.0] instanceof Integer[]);
//([1] instanceof Number[]);
