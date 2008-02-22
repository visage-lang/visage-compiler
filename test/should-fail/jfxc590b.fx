/*
 * Regression test: JFXC-590 :  Disallow "var" and "let" declarations in classes 
 * And, for this test, versa-versa no 'attribute' on local variables.
 *
 * @test/fail
 */

function foo() : Void {
   attribute y : Integer
}
