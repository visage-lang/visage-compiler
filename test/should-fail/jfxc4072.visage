/*
 * Regression test: Forward reference check overhaul
 * @compilearg -XDfwdRefError=false
 * @test/warning
 */

class A {
   var a:Integer = b; //warn
   var b:Integer = this.c; //warn
   var c:Integer = A.d; //warn
   var d:Integer = jfxc4072.A.e; //warn
   var e:Integer = 1 on replace { f; this.f; A.f; jfxc4072.A.f; e; this.e; A.e; jfxc4072.A.e};
   var f:Integer = 1 on invalidate { g; this.g; A.g; jfxc4072.A.g; };
   var g:function():Integer = function() { h; this.h; A.h; jfxc4072.A.h; g; this.g; A.g; jfxc4072.A.g; 1 }
   var h:Integer = { i; this.i; A.i; jfxc4072.A.i; h; this.h; A.h; jfxc4072.A.h; } //warn
   var i:Object[] = [a => 1, b => this.l, c => A.l, d => jfxc4072.A.l];
   var l:Integer = 42;
   var m:Integer = m; //warn
   var n:Integer = this.n; //warn
   var o:Integer = A.o; //warn
   var p:Integer = jfxc4072.A.p; //warn

   function func() {
      var a1:Integer = b1; //warn
      var b1:Integer = 1 on replace { c1; b1; };
      var c1:Integer = 1 on invalidate { d1; };
      var d1:function():Integer = function() { e1; d1; 1 }
      var e1:Integer = { f1; e1; 1 } //warn
      var f1:Object[] = [a1 => g1];
      var g1:Integer = 42;
      var h1:Integer = h1; //warn
   }
}

var a:Integer = b; //warn
var b:Integer = jfxc4072.c; //warn
var c:Integer = 1 on replace { d; jfxc4072.d; c; jfxc4072.c; };
var d:Integer = 1 on invalidate { e; jfxc4072.e; };
var e:function():Integer = function() { f; jfxc4072.f; e; jfxc4072.e; 1};
var f:Integer = { g; jfxc4072.g; f; jfxc4072.f; 1}; //warn
var g:Object[] = [a => h, b => jfxc4072.h];
var h:Integer = 42;
var i:Integer = i; //warn
var j:Integer = jfxc4072.j; //warn

function func() {
   var a1:Integer = b1; //warn
   var b1:Integer = 1 on replace { c1; b1; };
   var c1:Integer = 1 on invalidate { d1 };
   var d1:function():Integer = function() { e1; d1; 1 }
   var e1:Integer = { f1; e1 } //warn
   var f1:Object[] = [a1 => g1];
   var g1:Integer = 42;
   var h1:Integer = h1; //warn
}
