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
        assertEquals(Sequences.box(new long[] { 1, 2, 3 }), 1L, 2L, 3L);
        assertEquals(Sequences.box(new int[] { 1, 2, 3 }), 1, 2, 3);
        assertEquals(Sequences.box(new short[] { 1, 2, 3 }), 1, 2, 3);
        assertEquals(Sequences.box(new char[] { 1, 2, 3 }), 1, 2, 3);
        assertEquals(Sequences.box(new byte[] { 1, 2, 3 }), 1, 2, 3);
        assertEquals(Sequences.box(new double[] { 1.0, 2.0, 3.0 }), 1.0, 2.0, 3.0);
        assertEquals(Sequences.box(new float[] { 1.0f, 2.0f, 3.0f }), 1.0, 2.0, 3.0);
        assertEquals(Sequences.box(new boolean[] { true, false, true } ), true, false, true);

        assertEquals(Sequences.unbox(Sequences.range(1, 3)), 1, 2, 3);
        assertEquals(Sequences.unbox(Sequences.range(1.0, 3.0)), 1.0, 2.0, 3.0);
        assertEquals(Sequences.unbox(Sequences.box(new boolean[] { true, false })), true, false);
        assertEquals(Sequences.unbox(Sequences.box(new long[] { 1, 2, 3})), 1L, 2L, 3L);
    }
}
