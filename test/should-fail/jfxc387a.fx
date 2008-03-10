/*
 * Regression test: JFXC-387: Compiler says boolean/int/double/java.lang.String when it means Boolean/Integer/Double/String
 *
 * @test/fail
 *
 * This should produce:
 *
 *     jfxc387a.fx:29: incompatible types
 *     found   : Boolean
 *     required: Number
 *     var foo : Number = true;
 *                        ^
 *     1 error
 *     
 * instead of
 *
 *     jfxc387a.fx:29: incompatible types
 *     found   : boolean
 *     required: double
 *     var foo : Number = true;
 *                        ^
 *     1 error
 * 
 * There doesn't seem to be a way to provide .EXPECTED files 
 * for tests that are expected to fail compilation.
 *
 */

var foo : Number = true;
