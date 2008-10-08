/*
 * @subtest Main.fx
 */

package class Moo {
    package var x : Integer;
    package var y : Integer;

    package function mooprintln() : Integer {
        println("{x}.{y}");
        return 0;
    }
}
