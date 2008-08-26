package javafx.lang;

/**
 *  Bit operations
 */

public class Bits {

    public static int shiftLeft(int value, int shift) {
        return value << shift;
    }

    public static int shiftRight(int value, int shift) {
        return value >> shift;
    }

    public static int bitOr(int value, int mask) {
        return value | mask;
    }

    public static int bitAnd(int value, int mask) {
        return value & mask;
    }
    
    public static int bitXor(int value, int mask) {
        return value ^ mask;
    }

    public static int complement(int value) {
        return ~value;
    }

    public static int add(int value, int mask) {
        return value | mask;
    }

    public static int remove(int value, int mask) {
        return value & ~mask;
    }

    public static boolean contains(int value, int mask) {
        return (value & mask) != 0;
    }


    public static long shiftLeft(long value, int shift) {
        return value << shift;
    }

    public static long shiftRight(long value, int shift) {
        return value >> shift;
    }

    public static long bitOr(long value, long mask) {
        return value | mask;
    }

    public static long bitAnd(long value, long mask) {
        return value & mask;
    }
    
    public static long bitXor(long value, long mask) {
        return value ^ mask;
    }

    public static long complement(long value) {
        return ~value ;
    }

    public static long add(long value, long mask) {
        return value | mask;
    }

    public static long remove(long value, long mask) {
        return value & ~mask;
    }

    public static boolean contains(long value, long mask) {
        return (value & mask) != 0;
    }

}
