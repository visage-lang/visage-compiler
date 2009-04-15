/*
 * Regression test: JFXC-2182: Variable Scope/Visibility issue
 *
 * @test/compile-error
 */

class Abc {
    function runnit() {
        if (true) {
            var a:Number = 1;
        }
        var a:Number = 2; // Even var a:Integer = 2; failed too.
        println(">> {a}");
    }
}

Abc{}.runnit();
