/* 
 * Regression test: JFXC-614: Incorrect error position
 *
 * @test/compile-error
 */

import java.lang.*;
var i = 0;
i = bar();

function bar():Void {
}
