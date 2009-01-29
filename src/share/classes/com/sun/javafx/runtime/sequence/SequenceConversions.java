/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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
        return new ArraySequence<T>(ti, values);
    }

    /** Convert a long[] to a Sequence<Long> */
    public static Sequence<Long> fromArray(long[] values) {
        if (values == null)
            return TypeInfo.Long.emptySequence;
        Long[] boxed = new Long[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = values[i];
        return new ArraySequence<Long>(TypeInfo.Long, boxed, values.length);
    }

    /** Convert an int[] to a Sequence<Integer> */
    public static Sequence<Integer> fromArray(int[] values) {
        if (values == null)
            return TypeInfo.Integer.emptySequence;
        Integer[] boxed = new Integer[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = values[i];
        return new ArraySequence<Integer>(TypeInfo.Integer, boxed, values.length);
    }

    /** Convert a short[] to a Sequence<Short> */
    public static Sequence<Short> fromArray(short[] values) {
        if (values == null)
            return TypeInfo.Short.emptySequence;
        Short[] boxed = new Short[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = (short) values[i];
        return new ArraySequence<Short>(TypeInfo.Short, boxed, values.length);
    }

    /** Convert a char[] to a Sequence<Character> */
    public static Sequence<Character> fromArray(char[] values) {
        if (values == null)
            return TypeInfo.Character.emptySequence;
        Character[] boxed = new Character[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = (char) values[i];
        return new ArraySequence<Character>(TypeInfo.Character, boxed, values.length);
    }

    /** Convert a byte[] to a Sequence<Byte> */
    public static Sequence<Byte> fromArray(byte[] values) {
        if (values == null)
            return TypeInfo.Byte.emptySequence;
        Byte[] boxed = new Byte[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = (byte) values[i];
        return new ArraySequence<Byte>(TypeInfo.Byte, boxed, values.length);
    }

    /** Convert a double[] to a Sequence<Double> */
    public static Sequence<Double> fromArray(double[] values) {
        if (values == null)
            return TypeInfo.Double.emptySequence;
        Double[] boxed = new Double[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = values[i];
        return new ArraySequence<Double>(TypeInfo.Double, boxed, values.length);
    }

    /** Convert a float[] to a Sequence<Float> */
    public static Sequence<Float> fromArray(float[] values) {
        if (values == null)
            return TypeInfo.Float.emptySequence;
        Float[] boxed = new Float[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = (float) values[i];
        return new ArraySequence<Float>(TypeInfo.Float, boxed, values.length);
    }

    /** Convert a boolean[] to a Sequence<Boolean> */
    public static Sequence<Boolean> fromArray(boolean[] values) {
        if (values == null)
            return TypeInfo.Boolean.emptySequence;
        Boolean[] boxed = new Boolean[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = values[i];
        return new ArraySequence<Boolean>(TypeInfo.Boolean, boxed, values.length);
    }

    /** Convert a Sequence<T> to an array */
    public static<T> T[] toArray(Sequence<T> seq) {
        T[] unboxed = Util.<T>newObjectArray(seq.size());
        int i=0;
        for (T val : seq) {
            unboxed[i++] = val;
        }
        return unboxed;
    }

    /** Convert a Sequence<Long> to an array */
    public static long[] toArray(Sequence<Long> seq) {
        long[] unboxed = new long[seq.size()];
        int i=0;
        for (Long val : seq) {
            unboxed[i++] = val;
        }
        return unboxed;
    }

    /** Convert a Sequence<Integer> to an array */
    public static int[] toArray(Sequence<Integer> seq) {
        int[] unboxed = new int[seq.size()];
        int i=0;
        for (Integer val : seq) {
            unboxed[i++] = val;
        }
        return unboxed;
    }

    /** Convert a Sequence<Double> to a double array */
    public static double[] toDoubleArray(Sequence<? extends Number> seq) {
        double[] unboxed = new double[seq.size()];
        int i=0;
        for (Number val : seq) {
            unboxed[i++] = val.doubleValue();
        }
        return unboxed;
    }

    /** Convert a Sequence<Double> to a float array */
    public static float[] toFloatArray(Sequence<? extends Number> seq) {
        float[] unboxed = new float[seq.size()];
        int i=0;
        for (Number val : seq) {
            unboxed[i++] = val.floatValue();
        }
        return unboxed;
    }

    /** Convert a Sequence<Boolean> to an array */
    public static boolean[] toArray(Sequence<Boolean> seq) {
        boolean[] unboxed = new boolean[seq.size()];
        int i=0;
        for (Boolean val : seq) {
            unboxed[i++] = val;
        }
        return unboxed;
    }
}
