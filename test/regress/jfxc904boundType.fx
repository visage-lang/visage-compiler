/**
 * Regression test JFXC-904 : Binding Overhaul: compiler support for remaining AST nodes
 * Specifically type test and type cast within a bind
 *
 * @test
 * @run
 */
import java.lang.System; 

var enableBindingOverhaul;

class A {}
class B extends A { attribute b = "box" }
class C extends B { attribute c = "see" }

let x : A = new A;
let isA = bind x instanceof A;
let isB = bind x instanceof B;
let isC = bind x instanceof C;
let asB = bind if (x instanceof B) x as B else []; // also test []
let asC = bind if (x instanceof C) x as C else null; // also test null

if (x instanceof A) {
   System.out.println("Yep, an A")
}
if (x instanceof B) {
   System.out.println("FAIL, not a B")
} else {
   System.out.println("Correct, not a B -- {asB}")
}
if (x instanceof C) {
   System.out.println("FAIL, not a C")
} else {
   System.out.println("Correct, not a C -- {asC}")
}

x  = B {b: "be"};

if (x instanceof A) {
   System.out.println("Yep, an A")
}
if (x instanceof B) {
   System.out.println("Yep, a B -- {asB.b}")
}
if (x instanceof C) {
   System.out.println("FAIL, not a C")
} else {
   System.out.println("Correct, not a C -- {asC}")
}

x  = C {b: "bee"};

if (x instanceof A) {
   System.out.println("Yep, an A")
}
if (x instanceof B) {
   System.out.println("Yep, a B -- {asB.b}")
}
if (x instanceof C) {
   System.out.println("Yep, a C -- {asC.c}")
}

// test bound sequence construction
//TODO pending the fix to JFXC-925
//let fs = bind for (k in [1..5]) if (k < 3) [k, k] else [];
//System.out.println(fs)

