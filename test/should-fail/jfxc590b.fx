/*
 * Regression test: JFXC-590 :  Disallow "var" and "let" declarations in classes 
 * And, for this test, versa-versa no 'var' on local variables.
 *
 * @test/fail
 */

function foo() : Void {
   var y : Integer
}
