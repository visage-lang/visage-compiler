/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.util.concurrent.atomic.AtomicInteger;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.NumericTypeInfo;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.location.SequenceChangeListener;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceVariable;

/**
 * NumberSequenceTest
 *
 * @author Brian Goetz
 * @author Per Bothner
 */
public class NumberSequenceTest extends JavaFXTestCase {
    private final Sequence<Double> EMPTY_SEQUENCE = TypeInfo.Double.emptySequence;
    private final Sequence<Double> ZERO_SEQUENCE = new ArraySequence<Double>(TypeInfo.Double, 0.0).noteShared();

    /** Test ranges, including skip ranges and backwards ranges */
    public void testRange() {
        // [ 0..0 ] => [ 0 ]
        assertEquals(Sequences.range(0.0, 0.0, 1.0), 0.0);
        // [ 0..<0 ] => [ 0 ]
        assertEquals(Sequences.rangeExclusive(0.0, 0.0, 1.0), EMPTY_SEQUENCE);
        // [ 0..-1 ] => [ ]
        assertEquals(Sequences.range(0.0, -1.0, 1.0), EMPTY_SEQUENCE);
        // [ 0..0 STEP 3 ] => [ 0 ]
        assertEquals(Sequences.range(0.0, 0.0, 3.0), 0.0);
        // [ 0..<0 STEP 3 ] => [ 0 ]
        assertEquals(Sequences.rangeExclusive(0.0, 0.0, 3.0), EMPTY_SEQUENCE);
        // [ 0..1 ] => [ 0, 1 ]
        assertEquals(Sequences.range(0.0, 1.0, 1.0), 0.0, 1.0);
        // [ 0..<1 ] => [ 0 ]
        assertEquals(Sequences.rangeExclusive(0.0, 1.0, 1.0), 0.0);
        // [ 0..1 STEP 2 ] => [ 0 ]
        assertEquals(Sequences.range(0.0, 1.0, 2.0), 0.0);
        // [ 1..3 STEP 2 ] => [ 1, 3 ]
        assertEquals(Sequences.range(1.0, 3.0, 2.0), 1.0, 3.0);
        // [ 1..<3 STEP 2 ] => [ 1 ]
        assertEquals(Sequences.rangeExclusive(1.0, 3.0, 2.0), 1.0);
        // [ 1..4 STEP 2 ] => [ 1, 3 ]
        assertEquals(Sequences.range(1.0, 4.0, 2.0), 1.0, 3.0);
        // [ 1..<4 STEP 2 ] => [ 1, 3 ]
        assertEquals(Sequences.rangeExclusive(1.0, 4.0, 2.0), 1.0, 3.0);

        // [ 5..3 ] => [ 5, 4, 3 ]
        assertEquals(Sequences.range(5.0, 3.0, -1.0), 5.0, 4.0, 3.0);
        // [ 5..>3 ] => [ 5, 4 ]
        assertEquals(Sequences.rangeExclusive(5.0, 3.0, -1.0), 5.0, 4.0);
        // [ 5..3 STEP 2 ] => [ 5, 3 ]
        assertEquals(Sequences.range(5.0, 3.0, -2.0), 5.0, 3.0);
        // [ 5..>3 STEP 2 ] => [ 5 ]
        assertEquals(Sequences.rangeExclusive(5.0, 3.0, -2.0), 5.0 );
        // [ 5..2 STEP 2 ] => [ 5, 3 ]
        assertEquals(Sequences.range(5.0, 2.0, -2.0), 5.0, 3.0);
        // [ 5..>2 STEP 2 ] => [ 5, 3 ]
        assertEquals(Sequences.rangeExclusive(5.0, 2.0, -2.0), 5.0, 3.0);

        // [ 0..1 by .5 ] => [ 0, .5, 1 ]
        assertEquals(Sequences.range(0.0, 1.0, 0.5), 0.0, 0.5, 1.0);
        // [ 0..1 by .2 ] => [ 0, .2, .4, .6, .8, 1 ]
        assertEquals(Sequences.range(0.0, 1.0, 0.2), 0.0, 0.2, 0.4, 0.6, 0.8, 1.0);
        // [ 1..0 by -.2 ] => [ 1, .8, .6., .4, .2, 0 ]
        assertEquals(Sequences.range(1.0, 0.0, -0.2), 1.0, 0.8, 0.6, 0.4, 0.2, 0.0);
        // [ 0..<1 by .2 ] => [ 0, .2, .4, .6, .8, 1 ]
        assertEquals(Sequences.rangeExclusive(0.0, 1.0, 0.2), 0.0, 0.2, 0.4, 0.6, 0.8);
        // [ 1..<0 by -.2 ] => [ 1, .8, .6., .4, .2, 0 ]
        assertEquals(Sequences.rangeExclusive(1.0, 0.0, -0.2), 1.0, 0.8, 0.6, 0.4, 0.2);
        // [ 0..1 by .3 ] => [ 0, .3, .6, .9 ]
        assertEquals(Sequences.range(0.0, 1.0, 0.3), 0.0, 0.3, 0.6, 0.9);
        // [ 0..<1 by .3 ] => [ 0, .3, .6, .9 ]
        assertEquals(Sequences.rangeExclusive(0.0, 1.0, 0.3), 0.0, 0.3, 0.6, 0.9);
    }

    /** Test ranges, including skip ranges and backwards ranges */
    public void testRangeToArray() {
        Double[] actuals = new Double[0];
        // [ 0..-1 ] => [ ]
        Sequences.range(0.0, -1.0, 1.0).toArray(0, 0, actuals, 0);
        assertArrayEquals(new Double[0], actuals);
        // [ 0..<0 ] => [ ]
        Sequences.rangeExclusive(0.0, 0.0, 1.0).toArray(0, 0, actuals, 0);
        assertArrayEquals(new Double[0], actuals);
        // [ 0..<0 STEP 3 ] => [ 0 ]
        Sequences.rangeExclusive(0.0, 0.0, 3.0).toArray(0, 0, actuals, 0);
        assertArrayEquals(new Double[0], actuals);

        actuals = new Double[1];
        // [ 0..0 ] => [ 0 ]
        Sequences.range(0.0, 0.0, 1.0).toArray(0, 1, actuals, 0);
        assertArrayEquals(new Double[] {0.0}, actuals);
        // [ 0..0 STEP 3 ] => [ 0 ]
        Sequences.range(0.0, 0.0, 3.0).toArray(0, 1, actuals, 0);
        assertArrayEquals(new Double[] {0.0}, actuals);
        // [ 0..<1 ] => [ 0 ]
        Sequences.rangeExclusive(0.0, 1.0, 1.0).toArray(0, 1, actuals, 0);
        assertArrayEquals(new Double[] {0.0}, actuals);
        // [ 0..1 STEP 2 ] => [ 0 ]
        Sequences.range(0.0, 1.0, 2.0).toArray(0, 1, actuals, 0);
        assertArrayEquals(new Double[] {0.0}, actuals);
        // [ 1..<3 STEP 2 ] => [ 1 ]
        Sequences.range(1.0, 3.0, 2.0).toArray(0, 1, actuals, 0);
        assertArrayEquals(new Double[] {1.0}, actuals);
        // [ 5..>3 STEP 2 ] => [ 5 ]
        Sequences.rangeExclusive(5.0, 3.0, -2.0).toArray(0, 1, actuals, 0);
        assertArrayEquals(new Double[] {5.0}, actuals);
        
        actuals = new Double[2];
        // [ 0..1 ] => [ 0, 1 ]
        Sequences.range(0.0, 1.0, 1.0).toArray(0, 2, actuals, 0);
        assertArrayEquals(new Double[] {0.0, 1.0}, actuals);
        // [ 1..3 STEP 2 ] => [ 1, 3 ]
        Sequences.range(1.0, 3.0, 2.0).toArray(0, 2, actuals, 0);
        assertArrayEquals(new Double[] {1.0, 3.0}, actuals);
        // [ 1..4 STEP 2 ] => [ 1, 3 ]
        Sequences.range(1.0, 4.0, 2.0).toArray(0, 2, actuals, 0);
        assertArrayEquals(new Double[] {1.0, 3.0}, actuals);
        // [ 1..<4 STEP 2 ] => [ 1, 3 ]
        Sequences.rangeExclusive(1.0, 4.0, 2.0).toArray(0, 2, actuals, 0);
        assertArrayEquals(new Double[] {1.0, 3.0}, actuals);
        // [ 5..<3 ] => [ 5, 4 ]
        Sequences.rangeExclusive(5.0, 3.0, -1.0).toArray(0, 2, actuals, 0);
        assertArrayEquals(new Double[] {5.0, 4.0}, actuals);
        // [ 5..3 STEP 2 ] => [ 5, 3 ]
        Sequences.range(5.0, 3.0, -2.0).toArray(0, 2, actuals, 0);
        assertArrayEquals(new Double[] {5.0, 3.0}, actuals);
        // [ 5..2 STEP 2 ] => [ 5, 3 ]
        Sequences.range(5.0, 2.0, -2.0).toArray(0, 2, actuals, 0);
        assertArrayEquals(new Double[] {5.0, 3.0}, actuals);
        // [ 5..<2 STEP 2 ] => [ 5, 3 ]
        Sequences.rangeExclusive(5.0, 2.0, -2.0).toArray(0, 2, actuals, 0);
        assertArrayEquals(new Double[] {5.0, 3.0}, actuals);

        actuals = new Double[3];
        // [ 5..3 ] => [ 5, 4, 3 ]
        Sequences.range(5.0, 3.0, -1.0).toArray(0, 3, actuals, 0);
        assertArrayEquals(new Double[] {5.0, 4.0, 3.0}, actuals);
        // [ 0..1 STEP .5 ] => [ 0, .5, 1 ]
        Sequences.range(0.0, 1.0, 0.5).toArray(0, 3, actuals, 0);
        assertArrayEquals(new Double[] {0.0, 0.5, 1.0}, actuals);

        actuals = new Double[4];
        // [ 0..1 STEP .3 ] => [ 0, .3, .6, .9 ]
        Sequences.range(0.0, 1.0, 0.3).toArray(0, 4, actuals, 0);
        assertArrayEquals(new Double[] {0.0, 0.3, 0.6, 0.9}, actuals);
        // [ 0..<1 STEP .3 ] => [ 0, .3, .6, .9 ]
        Sequences.rangeExclusive(0.0, 1.0, 0.3).toArray(0, 4, actuals, 0);
        assertArrayEquals(new Double[] {0.0, 0.3, 0.6, 0.9}, actuals);

        actuals = new Double[5];
        // [ 0..<1 STEP .2 ] => [ 0, .2, .4, .6, .8, 1 ]
        Sequences.rangeExclusive(0.0, 1.0, 0.2).toArray(0, 5, actuals, 0);
        assertArrayEquals(new Double[] {0.0, 0.2, 0.4, 0.6, 0.8}, actuals);
        // [ 1..<0 STEP -.2 ] => [ 1, .8, .6., .4, .2, 0 ]
        Sequences.rangeExclusive(1.0, 0.0, -0.2).toArray(0, 5, actuals, 0);
        assertArrayEquals(new Double[] {1.0, 0.8, 0.6, 0.4, 0.2}, actuals);

        actuals = new Double[6];
        // [ 0..1 STEP .2 ] => [ 0, .2, .4, .6, .8, 1 ]
        Sequences.range(0.0, 1.0, 0.2).toArray(0, 6, actuals, 0);
        assertArrayEquals(new Double[] {0.0, 0.2, 0.4, 0.6, 0.8, 1.0}, actuals);
        // [ 1..0 STEP -.2 ] => [ 1, .8, .6., .4, .2, 0 ]
        Sequences.range(1.0, 0.0, -0.2).toArray(0, 6, actuals, 0);
        assertArrayEquals(new Double[] {1.0, 0.8, 0.6, 0.4, 0.2, 0.0}, actuals);

        // source-offset
        actuals = new Double[2];
        Sequence<Double> THREE_ELEMENTS = Sequences.range(1.0, 3.0, 1.0);
        THREE_ELEMENTS.toArray(0, 2, actuals, 0);
        assertArrayEquals(new Double[] {1.0, 2.0}, actuals);
        THREE_ELEMENTS.toArray(1, 2, actuals, 0);
        assertArrayEquals(new Double[] {2.0, 3.0}, actuals);
        
        actuals = new Double[2];
        try {
            THREE_ELEMENTS.toArray(-1, 2, actuals, 0);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException ex) {
            // ok
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.toString());
        }

        try {
            THREE_ELEMENTS.toArray(2, 2, actuals, 0);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException ex) {
            // ok
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.toString());
        }

        actuals = new Double[0];
        THREE_ELEMENTS.toArray(3, 0, actuals, 0);
        assertArrayEquals(new Double[0], actuals);

        
        // dest-offset
        actuals = new Double[4];
        actuals[0] = 2.0;
        THREE_ELEMENTS.toArray(0, 3, actuals, 1);
        assertArrayEquals(new Double[] {2.0, 1.0, 2.0, 3.0}, actuals);
        assertEquals(THREE_ELEMENTS, 1.0, 2.0, 3.0);

        actuals = new Double[3];
        try {
            THREE_ELEMENTS.toArray(0, 3, actuals, -1);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException ex) {
            // ok
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.toString());
        }
        try {
            THREE_ELEMENTS.toArray(0, 3, actuals, 1);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException ex) {
            // ok
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.toString());
        }
    }

    public void testMixedConcat () {
        TypeInfo<Number> NumberTypeInfo = TypeInfo.<Number>makeTypeInfo(0);

        Sequence<Integer> sI1 = new ArraySequence<Integer>(TypeInfo.Integer, 1, 2).noteShared();
        Sequence<Double> sD1 = new ArraySequence<Double>(TypeInfo.Double, 1.5, 2.5).noteShared();
        Sequence<Number> sN1 = Sequences.concatenate(NumberTypeInfo, sI1, sD1);
        Sequences.noteShared(sN1);
        assertEquals(sN1, 1, 2, 1.5, 2.5);
        assertEquals(Sequences.concatenate(TypeInfo.Integer, sI1, sI1), 1, 2, 1, 2);

        Sequence<Number> sN2 = Sequences.concatenate(NumberTypeInfo, sD1, sD1);
        assertEquals(sN2, 1.5, 2.5, 1.5, 2.5);

        Sequence<Double> sD2 = Sequences.concatenate(TypeInfo.Double, sD1, sD1);
        assertEquals(sD2, 1.5, 2.5, 1.5, 2.5);

        sN2 = Sequences.concatenate(NumberTypeInfo, sI1, sD1);
        assertEquals(sN2, 1, 2, 1.5, 2.5);

        sN2 = Sequences.concatenate(NumberTypeInfo, sD1, sI1);
        assertEquals(sN2, 1.5, 2.5, 1, 2);

        sN2 = Sequences.concatenate(NumberTypeInfo, sN1, sI1);
        assertEquals(sN2, 1, 2, 1.5, 2.5, 1, 2);

        sN2 = Sequences.concatenate(NumberTypeInfo, sD1, sN1);
        assertEquals(sN2, 1.5, 2.5, 1, 2, 1.5, 2.5);
    }

    public void testBoxing() {
        assertEquals(Sequences.fromArray(new long[] { 1, 2, 3 }), 1L, 2L, 3L);
        assertEquals(Sequences.fromArray(new int[] { 1, 2, 3 }), 1, 2, 3);
        assertEquals(Sequences.fromArray(new short[] { 1, 2, 3 }), (short)1, (short)2, (short)3);
        assertEquals(Sequences.fromArray(new char[] { 1, 2, 3 }), (char)1, (char)2, (char)3);
        assertEquals(Sequences.fromArray(new byte[] { 1, 2, 3 }), (byte)1, (byte)2, (byte)3);
        assertEquals(Sequences.fromArray(new double[] { 1.0, 2.0, 3.0 }), 1.0, 2.0, 3.0);
        assertEquals(Sequences.fromArray(new float[] { 1.0f, 2.0f, 3.0f }), 1.0f, 2.0f, 3.0f);
        assertEquals(Sequences.fromArray(new boolean[] { true, false, true } ), true, false, true);

        assertEquals(Sequences.toArray(Sequences.range(1, 3)), 1, 2, 3);
        assertEquals(Sequences.toDoubleArray(Sequences.range(1.0, 3.0)), 1.0, 2.0, 3.0);
        assertEquals(Sequences.toArray(Sequences.fromArray(new boolean[] { true, false })), true, false);
        assertEquals(Sequences.toArray(Sequences.fromArray(new long[] { 1, 2, 3})), 1L, 2L, 3L);
    }

    public void testOverflow() {
        assertThrows(IllegalArgumentException.class, new VoidCallable() {
            public void call() throws Exception {
                Sequence<Double> seq = Sequences.range(1.0, 1000000000.0, .01);
            }
        });
        Sequence<Double> seq = Sequences.range(1.0, Integer.MAX_VALUE, 1.0);
        assertEquals(seq.size(), Integer.MAX_VALUE);
    }

    public void testConversions() {
        Sequence<Byte> byteSeq = Sequences.fromArray(new byte[] { 1, 2, 3 });
        Sequence<Short> shortSeq = Sequences.fromArray(new short[] { 1, 2, 3 });
        Sequence<Integer> intSeq = Sequences.fromArray(new int[] { 1, 2, 3 });
        Sequence<Long> longSeq = Sequences.fromArray(new long[] { 1, 2, 3 });
        Sequence<Float> floatSeq = Sequences.fromArray(new float[] { 1.1f, 2.1f, 3.1f });
        Sequence<Double> doubleSeq = Sequences.fromArray(new double[] { 1.1, 2.1, 3.1 });

        assertEquals(Sequences.convertNumberSequence(TypeInfo.Byte, TypeInfo.Byte, byteSeq), (byte) 1, (byte) 2, (byte) 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Short, TypeInfo.Byte, byteSeq), (short) 1, (short) 2, (short) 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Integer, TypeInfo.Byte, byteSeq), 1, 2, 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Long, TypeInfo.Byte, byteSeq), 1L, 2L, 3L);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Float, TypeInfo.Byte, byteSeq), 1.0f, 2.0f, 3.0f);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Double, TypeInfo.Byte, byteSeq), 1.0, 2.0, 3.0);

        assertEquals(Sequences.convertNumberSequence(TypeInfo.Byte, TypeInfo.Short, shortSeq), (byte) 1, (byte) 2, (byte) 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Short, TypeInfo.Short, shortSeq), (short) 1, (short) 2, (short) 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Integer, TypeInfo.Short, shortSeq), 1, 2, 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Long, TypeInfo.Short, shortSeq), 1L, 2L, 3L);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Float, TypeInfo.Short, shortSeq), 1.0f, 2.0f, 3.0f);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Double, TypeInfo.Short, shortSeq), 1.0, 2.0, 3.0);

        assertEquals(Sequences.convertNumberSequence(TypeInfo.Byte, TypeInfo.Integer, intSeq), (byte) 1, (byte) 2, (byte) 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Short, TypeInfo.Integer, intSeq), (short) 1, (short) 2, (short) 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Integer, TypeInfo.Integer, intSeq), 1, 2, 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Long, TypeInfo.Integer, intSeq), 1L, 2L, 3L);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Float, TypeInfo.Integer, intSeq), 1.0f, 2.0f, 3.0f);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Double, TypeInfo.Integer, intSeq), 1.0, 2.0, 3.0);

        assertEquals(Sequences.convertNumberSequence(TypeInfo.Byte, TypeInfo.Long, longSeq), (byte) 1, (byte) 2, (byte) 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Short, TypeInfo.Long, longSeq), (short) 1, (short) 2, (short) 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Integer, TypeInfo.Long, longSeq), 1, 2, 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Long, TypeInfo.Long, longSeq), 1L, 2L, 3L);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Float, TypeInfo.Long, longSeq), 1.0f, 2.0f, 3.0f);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Double, TypeInfo.Long, longSeq), 1.0, 2.0, 3.0);

        assertEquals(Sequences.convertNumberSequence(TypeInfo.Byte, TypeInfo.Float, floatSeq), (byte) 1, (byte) 2, (byte) 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Short, TypeInfo.Float, floatSeq), (short) 1, (short) 2, (short) 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Integer, TypeInfo.Float, floatSeq), 1, 2, 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Long, TypeInfo.Float, floatSeq), 1L, 2L, 3L);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Float, TypeInfo.Float, floatSeq), 1.1f, 2.1f, 3.1f);
        // assertEquals(Sequences.convertNumberSequence(TypeInfo.Double, TypeInfo.Float, floatSeq), 1.1, 2.1, 3.1);

        assertEquals(Sequences.convertNumberSequence(TypeInfo.Byte, TypeInfo.Double, doubleSeq), (byte) 1, (byte) 2, (byte) 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Short, TypeInfo.Double, doubleSeq), (short) 1, (short) 2, (short) 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Integer, TypeInfo.Double, doubleSeq), 1, 2, 3);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Long, TypeInfo.Double, doubleSeq), 1L, 2L, 3L);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Float, TypeInfo.Double, doubleSeq), 1.1f, 2.1f, 3.1f);
        assertEquals(Sequences.convertNumberSequence(TypeInfo.Double, TypeInfo.Double, doubleSeq), 1.1, 2.1, 3.1);

        assertEquals(byteSeq, Sequences.convertNumberSequence(TypeInfo.Byte, TypeInfo.Byte, byteSeq));
        assertEquals(shortSeq, Sequences.convertNumberSequence(TypeInfo.Short, TypeInfo.Short, shortSeq));
        assertEquals(intSeq, Sequences.convertNumberSequence(TypeInfo.Integer, TypeInfo.Integer, intSeq));
        assertEquals(longSeq, Sequences.convertNumberSequence(TypeInfo.Long, TypeInfo.Long, longSeq));
        assertEquals(floatSeq, Sequences.convertNumberSequence(TypeInfo.Float, TypeInfo.Float, floatSeq));
        assertEquals(doubleSeq, Sequences.convertNumberSequence(TypeInfo.Double, TypeInfo.Double, doubleSeq));
    }

    public void testBoundConversions() {
        Sequence<Byte> byteSeq = Sequences.fromArray(new byte[] { 1, 2, 3 });
        Sequence<Short> shortSeq = Sequences.fromArray(new short[] { 1, 2, 3 });
        Sequence<Integer> intSeq = Sequences.fromArray(new int[] { 1, 2, 3 });
        Sequence<Long> longSeq = Sequences.fromArray(new long[] { 1, 2, 3 });
        Sequence<Float> floatSeq = Sequences.fromArray(new float[] { 1.0f, 2.0f, 3.0f });
        Sequence<Double> doubleSeq = Sequences.fromArray(new double[] { 1.0, 2.0, 3.0 });

        SequenceLocation<Byte> byteLoc = SequenceVariable.make(TypeInfo.Byte, byteSeq);
        SequenceLocation<Short> shortLoc  = SequenceVariable.make(TypeInfo.Short, shortSeq);
        SequenceLocation<Integer> intLoc = SequenceVariable.make(TypeInfo.Integer, intSeq);
        SequenceLocation<Long> longLoc = SequenceVariable.make(TypeInfo.Long, longSeq);
        SequenceLocation<Float> floatLoc = SequenceVariable.make(TypeInfo.Float, floatSeq);
        SequenceLocation<Double> doubleLoc = SequenceVariable.make(TypeInfo.Double, doubleSeq);

        final NumericTypeInfo[] tis = { TypeInfo.Byte, TypeInfo.Short, TypeInfo.Integer, TypeInfo.Long, TypeInfo.Float, TypeInfo.Double };
        final Sequence[] seqs = { byteSeq, shortSeq, intSeq, longSeq, floatSeq, doubleSeq };
        final SequenceLocation[] locs = { byteLoc, shortLoc, intLoc, longLoc, floatLoc, doubleLoc };
        SequenceLocation[][] converted = new SequenceLocation[6][6];

        final AtomicInteger count = new AtomicInteger();
        for (int i=0; i<tis.length; i++) {
            converted[i] = new SequenceLocation[6];
            for (int j=0; j<tis.length; j++) {
                converted[i][j] = BoundSequences.convertNumberSequence(tis[i], tis[j], locs[j]);
                final int j1 = j;
                converted[i][j].addChangeListener(new SequenceChangeListener() {
                    public void onChange(int startPos, int endPos, Sequence newElements, Sequence oldValue, Sequence newValue) {
                        count.incrementAndGet();
                        assertEquals(1, newElements.size());
                        assertEquals(3, startPos);
                        assertEquals(2, endPos);
                        assertEquals(4, tis[j1].intValue((Number) newValue.get(3)));
                    }
                });
            }
        }

        assertEquals(0, count.get());
        byteLoc.insert((byte) 4);
        shortLoc.insert((short) 4);
        intLoc.insert(4);
        longLoc.insert(4L);
        floatLoc.insert(4.0f);
        doubleLoc.insert(4.0);
        assertEquals(36, count.get());
    }
}
