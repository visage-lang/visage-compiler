/* regression test for the bug 875
 *
 * @test
 * @run
 */
import java.lang.System;

System.out.println("sizeof __ARGS__={sizeof __ARGS__}");
for (s in __ARGS__)
    System.out.println("s={s}");
