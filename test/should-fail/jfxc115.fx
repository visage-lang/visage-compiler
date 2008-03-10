/*
 * Regression test: JFXC-115: misleading error message from javafxc
 *
 * @test/fail
 *
 * This should produce:
 *
 *     jfxc115.fx:29: incompatible types
 *     found   : String
 *     required: Number
 *     var x:Number = new String("ss");
 *                    ^
 *     1 error
 *     
 * instead of
 *
 *     jfxc115.fx:29: incompatible types
 *     found   : java.lang.String
 *     required: double
 *     var x:Number = new String("ss");
 *                    ^
 *     1 error
 *      *
 * There doesn't seem to be a way to provide .EXPECTED files 
 * for tests that are expected to fail compilation.
 *
 */

var x:Number = new String("ss");
