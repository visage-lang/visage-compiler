/*
 * Regression test: JFXC-614: Incorrect error position
 *
 * @test/error
 *
 * This should produce:
 *
 *     jfxc614a.fx:31: incompatible types
 *     found   : void
 *     required: Integer
 *     i = bar();
 *            ^
 *     1 error
 * 
 * instead of
 *
 *     jfxc614a.fx:31: incompatible types
 *     found   : void
 *     required: int
 *     i = bar();
 *            ^
 *     1 error
 *
 * There doesn't seem to be a way to provide .EXPECTED files 
 * for tests that are expected to fail compilation.
 *
 */

import java.lang.*;
var i = 0;
i = bar();

function bar():Void {
}
