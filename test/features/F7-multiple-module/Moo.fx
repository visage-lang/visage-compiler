import java.lang.System;

/*
 * @subtest Main.fx
 */

package class Moo {
    package var x : Integer;
    package var y : Integer;

    package function println() : Integer {
        System.out.println("{x}.{y}");
        return 0;
    }
}
