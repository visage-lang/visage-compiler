/* Regression test for JFXC-870 : NPE thrown during compilation
 *
 * @test
 */

import java.lang.System;

var b = true;
var isVisible = bind b;
class A {
    attribute action: function();
}
A {
    action: function() {
        var c = isVisible;
    }
}
