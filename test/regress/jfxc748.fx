/*
 * @test
 * @run
 */

import java.lang.System;

var bug : Bug = null;
class Bug {
    attribute foo:String = "doesn't work.";
    static attribute bar = java.lang.Runnable {
        public function run():Void {
            var b = Bug{};
            b.foo = "works!";
            bug = b;
        }
    };
}
Bug.bar.run();
System.out.println("bug now {bug.foo}");
