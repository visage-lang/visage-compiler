/*
 * @subtest jfxc4173
 */

class A {
   var a;
   var b;
   var c;
   var d;
   var e:Integer = 1 on replace { f; this.f; A.f; jfxc4173sub.A.f; e; this.e; A.e; jfxc4173sub.A.e};
   var f:Integer = 1 on invalidate { g; this.g; A.g; jfxc4173sub.A.g; };
   var g:function():Integer = function() { i; this.i; A.i; jfxc4173sub.A.i; g; this.g; A.g; jfxc4173sub.A.g; 1 }
   var i:Object[] = [a => l, b => this.l, c => A.l, d => jfxc4173sub.A.l];
   var l:Integer = 42;

   function func() {
      var a1;
      var b1:Integer = 1 on replace { c1; b1; };
      var c1:Integer = 1 on invalidate { d1; };
      var d1:function():Integer = function() { f1; d1; 1 }
      var f1:Object[] = [a1 => g1];
      var g1:Integer = 42;
   }
}

var a;
var b;
var c:Integer = 1 on replace { d; jfxc4173sub.d; c; jfxc4173sub.c; };
var d:Integer = 1 on invalidate { e; jfxc4173sub.e; };
var e:function():Integer = function() { g; jfxc4173sub.g; e; jfxc4173sub.e; 1};
var g:Object[] = [a => h, b => jfxc4173sub.h];
var h:Integer = 42;

function func() {
   var a1;
   var b1:Integer = 1 on replace { c1; b1; };
   var c1:Integer = 1 on invalidate { d1 };
   var d1:function():Integer = function() { f1; d1; 1 }
   var f1:Object[] = [a1 => g1];
   var g1:Integer = 42;
}
