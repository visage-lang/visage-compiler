/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

import com.sun.javafx.runtime.JavaFXTestCase;

/**
 * NumberSequenceTest
 *
 * @author Brian Goetz
 * @author Per Bothner
 */
public class NumberSequenceTest extends JavaFXTestCase {
    private final Sequence<Double> EMPTY_SEQUENCE = EmptySequence.get(Double.class);
    private final Sequence<Double> ZERO_SEQUENCE = new ArraySequence<Double>(Double.class, 0.0);

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

    public void testMixedConcat () {
        Sequence<Integer> sI1 = new ArraySequence<Integer>(Integer.class, 1, 2);
        Sequence<Double> sD1 = new ArraySequence<Double>(Double.class, 1.5, 2.5);
        Sequence<Number> sN1 = Sequences.concatenate(Number.class, sI1, sD1);
        assertEquals(sN1, 1, 2, 1.5, 2.5);
        assertEquals(Sequences.concatenate(Integer.class, sI1, sI1), 1, 2, 1, 2);

        Sequence<Number> sN2 = Sequences.concatenate(Number.class, sD1, sD1);
        assertEquals(sN2, 1.5, 2.5, 1.5, 2.5);

        Sequence<Double> sD2 = Sequences.concatenate(Double.class, sD1, sD1);
        assertEquals(sD2, 1.5, 2.5, 1.5, 2.5);

        sN2 = Sequences.concatenate(Number.class, sI1, sD1);
        assertEquals(sN2, 1, 2, 1.5, 2.5);

        sN2 = Sequences.concatenate(Number.class, sD1, sI1);
        assertEquals(sN2, 1.5, 2.5, 1, 2);

        sN2 = Sequences.concatenate(Number.class, sN1, sI1);
        assertEquals(sN2, 1, 2, 1.5, 2.5, 1, 2);

        sN2 = Sequences.concatenate(Number.class, sD1, sN1);
        assertEquals(sN2, 1.5, 2.5, 1, 2, 1.5, 2.5);
    }

    public void testBoxing() {
        assertEquals(Sequences.fromArray(new long[] { 1, 2, 3 }), 1L, 2L, 3L);
        assertEquals(Sequences.fromArray(new int[] { 1, 2, 3 }), 1, 2, 3);
        assertEquals(Sequences.fromArray(new short[] { 1, 2, 3 }), 1, 2, 3);
        assertEquals(Sequences.fromArray(new char[] { 1, 2, 3 }), 1, 2, 3);
        assertEquals(Sequences.fromArray(new byte[] { 1, 2, 3 }), 1, 2, 3);
        assertEquals(Sequences.fromArray(new double[] { 1.0, 2.0, 3.0 }), 1.0, 2.0, 3.0);
        assertEquals(Sequences.fromArray(new float[] { 1.0f, 2.0f, 3.0f }), 1.0, 2.0, 3.0);
        assertEquals(Sequences.fromArray(new boolean[] { true, false, true } ), true, false, true);

        assertEquals(Sequences.toArray(Sequences.range(1, 3)), 1, 2, 3);
        assertEquals(Sequences.toArray(Sequences.range(1.0, 3.0)), 1.0, 2.0, 3.0);
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

}
