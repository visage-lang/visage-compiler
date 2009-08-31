/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.Util;

/**
 * SequenceConversions
 *
 * @author Brian Goetz
 */
public class SequenceConversions {
    /*******************************************/
    /* Converting between sequences and arrays */
    /*******************************************/

    /** Convert a T[] to a Sequence<T> */
    public static<T> Sequence<T> fromArray(TypeInfo<T, ?> ti, T[] values) {
        if (values == null)
            return ti.emptySequence;
        return new ObjectArraySequence<T>(ti, values);
    }

    /** Convert a long[] to a Sequence<Long> */
    public static Sequence<Long> fromArray(long[] values) {
        if (values == null)
            return TypeInfo.Long.emptySequence;
        return new LongArraySequence(values, 0, values.length);
    }

    /** Convert an int[] to a Sequence<Integer> */
    public static Sequence<Integer> fromArray(int[] values) {
        if (values == null)
            return TypeInfo.Integer.emptySequence;
        return new IntArraySequence(values, 0, values.length);
    }

    /** Convert a short[] to a Sequence<Short> */
    public static Sequence<Short> fromArray(short[] values) {
        if (values == null)
            return TypeInfo.Short.emptySequence;
        return new ShortArraySequence(values, 0, values.length);
    }

    /** Convert a char[] to a Sequence<Character> */
    public static Sequence<Character> fromArray(char[] values) {
        if (values == null)
            return TypeInfo.Character.emptySequence;
        return new CharArraySequence(values, 0, values.length);
    }

    /** Convert a byte[] to a Sequence<Byte> */
    public static Sequence<Byte> fromArray(byte[] values) {
        if (values == null)
            return TypeInfo.Byte.emptySequence;
        return new ByteArraySequence(values, 0, values.length);
    }

    /** Convert a double[] to a Sequence<Double> */
    public static Sequence<Double> fromArray(double[] values) {
        if (values == null)
            return TypeInfo.Double.emptySequence;
        return new DoubleArraySequence(values, 0, values.length);
    }

    /** Convert a float[] to a Sequence<Float> */
    public static Sequence<Float> fromArray(float[] values) {
        if (values == null)
            return TypeInfo.Float.emptySequence;
        return new FloatArraySequence(values, 0, values.length);
    }

    /** Convert a boolean[] to a Sequence<Boolean> */
    public static Sequence<Boolean> fromArray(boolean[] values) {
        if (values == null)
            return TypeInfo.Boolean.emptySequence;
        return new BooleanArraySequence(values, 0, values.length);
    }

    /** Convert a Sequence<T> to an array */
    public static<T> T[] toArray(Sequence<? extends T> seq) {
        T[] unboxed = Util.<T>newObjectArray(seq.size());
        int i=0;
        for (T val : seq) {
            unboxed[i++] = val;
        }
        return unboxed;
    }

    /** Convert a Sequence<Long> to an array */
    public static long[] toLongArray(Sequence<? extends Number> seq) {
        int size = seq.size();
        long[] unboxed = new long[size];
        for (int i = size;  --i >= 0; )
            unboxed[i] = seq.getAsLong(i);
        return unboxed;
    }

    /** Convert a Sequence<Integer> to an array */
    public static int[] toIntArray(Sequence<? extends Number> seq) {
        int size = seq.size();
        int[] unboxed = new int[size];
        for (int i = size;  --i >= 0; )
            unboxed[i] = seq.getAsInt(i);
        return unboxed;
    }

    /** Convert a Sequence<Short> to an array */
    public static short[] toShortArray(Sequence<? extends Number> seq) {
        int size = seq.size();
        short[] unboxed = new short[size];
        for (int i = size;  --i >= 0; )
            unboxed[i] = seq.getAsShort(i);
        return unboxed;
    }

    /** Convert a Sequence<Byte> to an array */
    public static byte[] toByteArray(Sequence<? extends Number> seq) {
        int size = seq.size();
        byte[] unboxed = new byte[size];
        for (int i = size;  --i >= 0; )
            unboxed[i] = seq.getAsByte(i);
        return unboxed;
    }

    /** Convert a Sequence<Double> to a double array */
    public static double[] toDoubleArray(Sequence<? extends Number> seq) {
        int size = seq.size();
        double[] unboxed = new double[size];
        for (int i = size;  --i >= 0; )
            unboxed[i] = seq.getAsDouble(i);
        return unboxed;
    }

    /** Convert a Sequence<Double> to a float array */
    public static float[] toFloatArray(Sequence<? extends Number> seq) {
        int size = seq.size();
        float[] unboxed = new float[size];
        for (int i = size;  --i >= 0; )
            unboxed[i] = seq.getAsFloat(i);
        return unboxed;
    }

    /** Convert a Sequence<Boolean> to an array */
    public static boolean[] toBooleanArray(Sequence<? extends Boolean> seq) {
        int size = seq.size();
        boolean[] unboxed = new boolean[size];
        for (int i = size;  --i >= 0; )
            unboxed[i] = seq.getAsBoolean(i);
        return unboxed;
    }
}
