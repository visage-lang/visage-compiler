/*
 * Regression test for JFXC-2387 : reference to + is ambiguous, both method +(double,double) in and method +(int,int) in match
 *
 * @test
 */

import java.util.*;
import java.lang.System;

/* note: some legal cast have been commented because of a bug in translation - the runtime
currently do not support cast/instance test whose target type is a sequence type */

//reference types

mixin class A {}
mixin class B {}
class C extends A,B {}
var c = C{};
var b:B = c;
var a:A = c;
var x:ArrayList = new ArrayList();

(a as ArrayList);
(a as ArrayList[]);
//([a] as ArrayList[]);

(a as List);
(a as List[]);
//([a] as List[]);

(a as B);
(b as A);
//(b as A[]);


(a as B[]);
//([a] as B[]);
(b as A[]);

(x as Collection);
(x as Collection[]);
([x] as Collection[]);

//null

(null as Integer[]);

(null as A[]);
(null as A);

([] as A[]);


//primitives

(1 as Number);
(1.0 as Integer);
//(1.0 as Integer[]);
//(1 as Number[]);
//([1.0] as Integer[]);
//([1] as Number[]);
