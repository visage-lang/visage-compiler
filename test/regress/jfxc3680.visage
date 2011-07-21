/**
 * JFXC-3680 : Compiled-bind: where clause cause backend failure.
 *
 * @test
 */

public class jfxc3680 { 
   var a:A; 
} 

class A { 
   function f() { return true; } 
} 

var seq:jfxc3680[] = []; 

function test() { 
   for (t in seq where t.a.f()) { 
     t 
   } 
}
