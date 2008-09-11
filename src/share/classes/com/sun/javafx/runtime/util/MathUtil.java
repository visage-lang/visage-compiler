package com.sun.javafx.runtime.util;

/**
 * MathUtil
 *
 * @author Brian Goetz
 */
public class MathUtil {
    private static final byte[] lookupTable = new byte[256];

    /** Compute number of leading zeros for an unsigned int; from Hacker's Delight, fig 5-6 */
    public static int nlzByBinarySearch(/* unsigned */ int x) {
        if (x == 0)
            return 32;
        int n = 1;
        if ((x >>> 16) == 0) { n += 16; x <<= 16; }
        if ((x >>> 24) == 0) { n += 8; x <<= 8; }
        if ((x >>> 28) == 0) { n += 4; x <<= 4; }
        if ((x >>> 30) == 0) { n += 2; x <<= 2; }
        n -= (x >>> 31);
        return n;
    }

    public static int log2(/* unsigned */ int x) {
        return 32-nlzByBinarySearch(x);
    }
}
