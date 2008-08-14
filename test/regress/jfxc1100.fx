/*
 * Regression test JFXC-1100 : Using Number as a Sequence Index Causes a Compile Error
 *
 * @test
 * @run
 */

import java.lang.System;

class BugTest {
    attribute status: Number;
    attribute codes: Number[] = [0, 1, 2, 3];
    attribute ubtest: Number = codes[status];
    attribute btest: Number = bind codes[status];
}

var bt = BugTest { status: 2.2 }
System.out.println(bt.ubtest);
System.out.println(bt.btest);
