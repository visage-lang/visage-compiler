/*
 * Regression test: JFXC-846: Error message on the last line of a source file produces nulls in error message.
 *
 * @test/fail
 *
 * This should produce:
 *
 *     jfxc846.fx:38: cannot find symbol
 *     symbol: class foo
 *     import foo
 *            ^
 *     1 error
 * 
 * instead of
 * 
 *     jfxc846.fx:38: cannot find symbol
 *     symbol: class foo
 *     import foo^@^@^@^@^@^@^@^@^@^@
 *            ^
 *     1 error
 * 
 * where the '^@'"s are actually null bytes.  
 * This is javac error 
 *
 *     6668802: javac handles diagnostics for last line badly, if line not terminated by newline
 * 
 * and will be fixed when javafxc incorporates the fix for that bug.
 * 
 * This same input serves as a regression test for bug in javac.
 * One has to be careful when editing this file 
 * *not* to add a newline to the end of the file.
 *
 * There doesn't seem to be a way to provide .EXPECTED files 
 * for tests that are expected to fail compilation.
 *
 */

import foo