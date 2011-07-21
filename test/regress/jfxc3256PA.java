/*
 * Regression: JFXC-3256 - Compiler crash: bound sequence to primitive array conversion.
 *
 * @subtest
 *
 */

public class jfxc3256PA {
    public static int[] intArray(int[] array) {
        return array;
    }
}
