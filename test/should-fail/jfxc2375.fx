/*
 * Regression test: JFXC-2375: Compiler internal error with the script "this"
 *
 * @test/compile-error
 */

this;
{ this; }
