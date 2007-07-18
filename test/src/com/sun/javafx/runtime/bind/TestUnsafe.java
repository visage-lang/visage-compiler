package com.sun.javafx.runtime.bind;

import junit.framework.TestCase;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * TestUnsafe.  This is what is sometimes called an "exploration test"; it validates that our understanding
 * of the Unsafe API is correct.
 *
 * @author Brian Goetz
 */
public class TestUnsafe extends TestCase {
    static Unsafe unsafe = UnsafeUtil.getUnsafe();

    private long[] getOffsets(Class clazz, String[] fields) throws NoSuchFieldException {
        long[] result = new long[fields.length];
        for (int i=0; i<fields.length; i++) {
            Field field = clazz.getDeclaredField(fields[i]);
            assertNotNull(field);
            result[i] = unsafe.objectFieldOffset(field);
            assertTrue(result[i] > 0);
        }
        return result;
    }

    /**
     * Test that puts and gets to fields of an object agree with reads and writes of that field.
     */
    public void testSimpleGetPut() throws NoSuchFieldException {
        class Foo {
            int a;
            double b;
            String c;
        }
        long[] offsets = getOffsets(Foo.class, new String[] { "a", "b", "c" });

        Foo f = new Foo();
        f.a = 1;
        f.b = 2.0;
        f.c = "three";
        assertEquals(f.a, unsafe.getInt(f, offsets[0]));
        assertEquals(f.b, unsafe.getDouble(f, offsets[1]));
        assertEquals(f.c, unsafe.getObject(f, offsets[2]));

        unsafe.putInt(f, offsets[0], 3);
        unsafe.putDouble(f, offsets[1], 3.14);
        unsafe.putObject(f, offsets[2], "yada");
        assertEquals(f.a, 3);
        assertEquals(f.b, 3.14);
        assertEquals(f.c, "yada");
    }

    public void testSubclassFieldGet() throws NoSuchFieldException {
        class Foo {
            int a;
            double b;
            String c;
        }
        class Bar extends Foo {
        }
        long[] offsets = getOffsets(Foo.class, new String[] { "a", "b", "c" });

        Bar f = new Bar();
        f.a = 1;
        f.b = 2.0;
        f.c = "three";
        assertEquals(f.a, unsafe.getInt(f, offsets[0]));
        assertEquals(f.b, unsafe.getDouble(f, offsets[1]));
        assertEquals(f.c, unsafe.getObject(f, offsets[2]));

        unsafe.putInt(f, offsets[0], 3);
        unsafe.putDouble(f, offsets[1], 3.14);
        unsafe.putObject(f, offsets[2], "yada");
        assertEquals(f.a, 3);
        assertEquals(f.b, 3.14);
        assertEquals(f.c, "yada");
    }

    public void testArrayGetPut() {
        long array[] = new long[256];

        long arrayBase = unsafe.arrayBaseOffset(long[].class);
        assertTrue(arrayBase > 0);
        int arrayScale = unsafe.arrayIndexScale(long[].class);
        assertTrue(arrayScale > 0);

        for (int i=0; i<256; i++)
            array[i] = i-50;
        for (int i=0; i<256; i++) {
            assertEquals(array[i], unsafe.getLong(array, arrayBase+arrayScale*i));
            unsafe.putLong(array, arrayBase+arrayScale*i, i);
            assertEquals(array[i], i);
        }

        // Test storing ints
        for (int i=0; i<256; i++) {
            unsafe.putInt(array, arrayBase+arrayScale*i, i-50);
            assertEquals(i-50, unsafe.getInt(array, arrayBase+arrayScale*i));
        }

        // Test storing shorts
        for (short i=0; i<256; i++) {
            unsafe.putShort(array, arrayBase+arrayScale*i, (short) (i-50));
            assertEquals((short) (i-50), unsafe.getShort(array, arrayBase+arrayScale*i));
        }

        // Test storing bytes
        for (int i=0; i<256; i++) {
            unsafe.putByte(array, arrayBase+arrayScale*i, (byte) (i-50));
            assertEquals((byte) (i-50), unsafe.getByte(array, arrayBase+arrayScale*i));
        }

        array = null;
        System.gc();
    }

    public void testPrimitivesCompatible() {
        int longScale = unsafe.arrayIndexScale(long[].class);
        assertTrue(unsafe.arrayIndexScale(int[].class) <= longScale);
        assertTrue(unsafe.arrayIndexScale(float[].class) <= longScale);
        assertTrue(unsafe.arrayIndexScale(double[].class) <= longScale);
        assertTrue(unsafe.arrayIndexScale(boolean[].class) <= longScale);
        assertTrue(unsafe.arrayIndexScale(short[].class) <= longScale);
        assertTrue(unsafe.arrayIndexScale(byte[].class) <= longScale);
        assertTrue(unsafe.arrayIndexScale(char[].class) <= longScale); 
    }
}
