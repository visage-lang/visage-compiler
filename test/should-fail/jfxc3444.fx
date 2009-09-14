/**
 * Regress test for JFXC-3444: attribute invalidate x statements
 *
 * @test/compile-error
 */

var y = 1;

invalidate y; //cannot invalidate non-bound vars
invalidate 1; //invalidate argument must be a var

function f() {
   var x = 1;
   x;
   invalidate(x); //cannot invalidate local non-bound var
}
