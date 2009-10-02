/**
 * Regress test for JFXC-3467: Add hasAnInitializer(x) synthetic method
 *
 * @test/compile-error
 */

var x = 10;
println(hasAnInitializer(x)); //error x should be an instance var

function f() {
   var x = 10;
   println(hasAnInitializer(x)); //error x should be an instance var
}
