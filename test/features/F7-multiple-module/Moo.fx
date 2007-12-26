import java.lang.System;

/*
 * @subtest Main.fx
 */

class Moo {
    attribute x : Integer;
    attribute y : Integer;

    function println() : Integer {
        System.out.println("{x}.{y}");
        return 0;
    }
}
