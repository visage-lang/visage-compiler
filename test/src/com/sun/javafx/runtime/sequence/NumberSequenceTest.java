package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.JavaFXTestCase;

/**
 * NumberSequenceTest
 *
 * @author Brian Goetz
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
}
