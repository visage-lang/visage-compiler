/*
 * Regression test
 * JFXC-3924 : Unexpected type assertion in JavafxTreeMaker.java
 *
 * @test
 */

function m1():Object {
   if(true) { return null; }
   else if (false) { return null; }
   else null;
}

function m2():Object {
   if(true) { return null; }
   else if (false) { return null; }
   else [];
}
