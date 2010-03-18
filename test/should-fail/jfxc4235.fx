/**
 * Regression test JFXC-4235 : ClassCastException: com.sun.tools.mjavac.code.Symbol$MethodSymbol cannot be cast to com.sun.tools.javafx.code.JavafxVarSymbol
 *
 *  @test/compile-error
 */

class A {
   var f: function():Object[];
}

class B extends A {
   override var f = function() {[]};
   function f(x:Object[]) {[]}
}
