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

import java.util.BitSet;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.TypeInfos;

/**
 * TestIntegerSequence
 *
 * @author Brian Goetz
 */
public class IntegerSequenceTest extends JavaFXTestCase {

    private static final int A = 3;
    private static final int B = 5;
    private static final int C = 7;
    private final Sequence<Integer> EMPTY_SEQUENCE = TypeInfos.Integer.getEmptySequence();
    private final Sequence<Integer> ONE_SEQUENCE = new ArraySequence<Integer>(TypeInfos.Integer, A);
    private final Sequence<Integer> TWO_SEQUENCE = new ArraySequence<Integer>(TypeInfos.Integer, A, B);

    private final SequencePredicate<Integer> nullMatcher = new SequencePredicate<Integer>() {
        public boolean matches(Sequence<? extends Integer> sequence, int index, Integer value) {
            return false;
        }
    };
    private final SequencePredicate<Integer> allMatcher = new SequencePredicate<Integer>() {
        public boolean matches(Sequence<? extends Integer> sequence, int index, Integer value) {
            return true;
        }
    };
    private final SequencePredicate<Integer> firstMatcher = new SequencePredicate<Integer>() {
        public boolean matches(Sequence<? extends Integer> sequence, int index, Integer value) {
            return index == 0;
        }
    };
    private final SequencePredicate<Integer> lastMatcher = new SequencePredicate<Integer>() {
        public boolean matches(Sequence<? extends Integer> sequence, int index, Integer value) {
            return index == sequence.size() - 1;
        }
    };
    private final SequencePredicate<Integer> oddMatcher = new SequencePredicate<Integer>() {
        public boolean matches(Sequence<? extends Integer> sequence, int index, Integer value) {
            return value % 2 != 0;
        }
    };
    private final SequencePredicate<Integer> evenMatcher = new SequencePredicate<Integer>() {
        public boolean matches(Sequence<? extends Integer> sequence, int index, Integer value) {
            return value % 2 == 0;
        }
    };
    private final SequencePredicate<Integer> oneMatcher = new SequencePredicate<Integer>() {
        public boolean matches(Sequence<? extends Integer> sequence, int index, Integer value) {
            return value == 1;
        }
    };

    /**
     * Helper method for asserting the depth of a Sequence
     */
    private <T> void assertDepth(int depth, Sequence<T> seq) {
        assertEquals(depth, seq.getDepth());
    }


    /**
     * Assert various properties of a supposedly empty sequence
     */
    private void assertEmpty(final Sequence<? extends Integer> seq) {
        assertEquals(0, seq.size());
        assertTrue(seq.isEmpty());
        assertEquals(seq, seq);
        assertTrue(Sequences.isEqual(seq, null));
        assertTrue(Sequences.isEqual(null, seq));
        assertEquals(0, seq.hashCode());
        assertEquals(Integer.valueOf(0), seq.get(-1));
        assertEquals(Integer.valueOf(0), seq.get(0));
        assertEquals(Integer.valueOf(0), seq.get(1));
        assertEquals("[ ]", seq.toString());
        assertEquals(seq, EMPTY_SEQUENCE);
        assertEquals(seq, seq.get(nullMatcher));
        assertEquals(seq, seq.get(firstMatcher));
        assertEquals(seq, seq.get(lastMatcher));
        assertEquals(seq, seq.get(allMatcher));
        assertFalse(seq.equals(ONE_SEQUENCE));
    }

    /**
     * Assert various properties of a supposedly single-element sequence
     */
    private void assertOneElement(final Sequence<? extends Integer> seq, int value) {
        assertEquals(1, seq.size());
        assertFalse(seq.isEmpty());
        assertEquals(seq, seq);
        assertEquals(value, seq.hashCode());
        assertEquals("[ " + value + " ]", seq.toString());
        assertEquals(Integer.valueOf(0), seq.get(-1));
        assertEquals(Integer.valueOf(value), seq.get(0));
        assertEquals(Integer.valueOf(0), seq.get(1));
        assertEmpty(seq.get(nullMatcher));
        assertEquals(seq, seq.get(firstMatcher));
        assertEquals(seq, seq.get(lastMatcher));
        assertEquals(seq, seq.get(allMatcher));
        assertFalse(seq.equals(EMPTY_SEQUENCE));
    }

    /**
     * Assert various properties of a supposedly two-element sequence
     */
    private void assertTwoElements(final Sequence<? extends Integer> seq, Integer a, Integer b) {
        assertEquals(2, seq.size());
        assertFalse(seq.isEmpty());
        assertEquals(seq, seq);
        assertEquals(seq, new ArraySequence<Integer>(TypeInfos.Integer, seq.get(0), seq.get(1)));
        assertEquals("[ " + a + ", " + b + " ]", seq.toString());
        assertEquals(Integer.valueOf(0), seq.get(-1));
        assertEquals(a, seq.get(0));
        assertEquals(b, seq.get(1));
        assertEquals(Integer.valueOf(0), seq.get(2));
        assertEmpty(seq.get(nullMatcher));
        assertEquals(seq, seq.get(allMatcher));
        assertOneElement(seq.get(firstMatcher), a);
        assertOneElement(seq.get(lastMatcher), b);
        assertFalse(seq.equals(EMPTY_SEQUENCE));
        assertFalse(seq.equals(ONE_SEQUENCE));
    }


    /**
     * Helper method that tests mutation methods of supposedly empty sequences
     */
    private void emptyHelper(final Sequence<Integer> seq) {
        assertEmpty(seq);
        assertEmpty(seq.get(allMatcher));

        // Test positional insertion
        assertOneElement(Sequences.insert(seq, ((Integer) 3)), 3);
        assertOneElement(Sequences.insertFirst(seq, 3), 3);
        assertOneElement(Sequences.insert(seq, new SingletonSequence<Integer>(TypeInfos.Integer, 4)), 4);
        assertOneElement(Sequences.insertFirst(seq, new SingletonSequence<Integer>(TypeInfos.Integer, 4)), 4);

        // Test deletion
        assertEmpty(Sequences.delete(seq, (Integer) 0));
        assertEmpty(Sequences.delete(seq, (Integer) 1));
        assertEmpty(Sequences.delete(seq, -1));
        assertEmpty(Sequences.delete(seq, allMatcher));
        assertEmpty(Sequences.delete(seq, nullMatcher));
        assertEmpty(Sequences.delete(seq, firstMatcher));
        assertEmpty(Sequences.delete(seq, lastMatcher));

        assertEmpty(Sequences.reverse(seq));
        assertEmpty(seq.flatten());
        assertEquals(seq.flatten(), seq);
    }

    /**
     * Helper method that tests mutation methods of supposedly single-element sequences
     */
    private void oneElementHelper(Sequence<Integer> seq, int value) {
        assertOneElement(seq, value);

        // Test positional insertion
        assertTwoElements(Sequences.insert(seq, B), value, B);
        assertTwoElements(Sequences.insertFirst(seq, B), B, value);
        assertTwoElements(Sequences.insert(seq, new SingletonSequence<Integer>(TypeInfos.Integer, B)), value, B);
        assertTwoElements(Sequences.insertFirst(seq, new SingletonSequence<Integer>(TypeInfos.Integer, B)), B, value);

        // Test bulk extraction
        assertEmpty(seq.get(nullMatcher));
        assertOneElement(seq.get(allMatcher), value);
        assertOneElement(seq.get(firstMatcher), value);
        assertOneElement(seq.get(lastMatcher), value);

        // Test deletion
        assertEmpty(Sequences.delete(seq, 0));
        assertOneElement(Sequences.delete(seq, 1), value);
        assertOneElement(Sequences.delete(seq, -1), value);
        assertEmpty(Sequences.delete(seq, allMatcher));
        assertEmpty(Sequences.delete(seq, firstMatcher));
        assertEmpty(Sequences.delete(seq, lastMatcher));
        assertOneElement(Sequences.delete(seq, nullMatcher), value);

        assertEquals(Sequences.reverse(seq), value);
        assertEquals(seq.flatten(), value);
        assertDepth(0, seq.flatten());
    }

    private void twoElementHelper(Sequence<Integer> seq, Integer a, Integer b) {
        assertTwoElements(seq, a, b);

        // Test bulk extraction
        assertEmpty(seq.get(nullMatcher));
        assertTwoElements(seq.get(allMatcher), a, b);
        assertOneElement(seq.get(firstMatcher), a);
        assertOneElement(seq.get(lastMatcher), b);

        // Test deletion
        assertOneElement(Sequences.delete(seq, 0), b);
        assertOneElement(Sequences.delete(seq, 1), a);
        assertTwoElements(Sequences.delete(seq, 2), a, b);
        assertTwoElements(Sequences.delete(seq, -1), a, b);
        assertEmpty(Sequences.delete(seq, allMatcher));
        assertTwoElements(Sequences.delete(seq, nullMatcher), a, b);
        assertOneElement(Sequences.delete(seq, firstMatcher), b);
        assertOneElement(Sequences.delete(seq, lastMatcher), a);

        // Test positional insertion
        Sequence<Integer> cc = new ArraySequence<Integer>(TypeInfos.Integer, C, C);
        assertEquals(Sequences.insert(seq, C), a, b, C);
        assertEquals(Sequences.insert(seq, cc), a, b, C, C);
        assertEquals(Sequences.insertFirst(seq, C), C, a, b);
        assertEquals(Sequences.insertFirst(seq, cc), C, C, a, b);
        assertEquals(Sequences.insertBefore(seq, C, -10), a, b);
        assertEquals(Sequences.insertBefore(seq, cc, -10), a, b);
        assertEquals(Sequences.insertBefore(seq, C, 0), C, a, b);
        assertEquals(Sequences.insertBefore(seq, cc, 0), C, C, a, b);
        assertEquals(Sequences.insertBefore(seq, C, 1), a, C, b);
        assertEquals(Sequences.insertBefore(seq, cc, 1), a, C, C, b);
        assertEquals(Sequences.insertAfter(seq, C, 0), a, C, b);
        assertEquals(Sequences.insertAfter(seq, cc, 0), a, C, C, b);
        assertEquals(Sequences.insertAfter(seq, C, 1), a, b, C);
        assertEquals(Sequences.insertAfter(seq, cc, 1), a, b, C, C);
        assertEquals(Sequences.insertAfter(seq, C, 100), a, b);
        assertEquals(Sequences.insertAfter(seq, cc, 100), a, b);

        // Test predicate insertion 
        assertEquals(Sequences.insertAfter(seq, C, nullMatcher), seq);
        assertEquals(Sequences.insertAfter(seq, C, firstMatcher), a, C, b);
        assertEquals(Sequences.insertAfter(seq, C, lastMatcher), a, b, C);
        assertEquals(Sequences.insertAfter(seq, C, allMatcher), a, C, b, C);
        assertEquals(Sequences.insertBefore(seq, C, nullMatcher), seq);
        assertEquals(Sequences.insertBefore(seq, C, firstMatcher), C, a, b);
        assertEquals(Sequences.insertBefore(seq, C, lastMatcher), a, C, b);
        assertEquals(Sequences.insertBefore(seq, C, allMatcher), C, a, C, b);

        assertEquals(Sequences.insertAfter(seq, cc, nullMatcher), seq);
        assertEquals(Sequences.insertAfter(seq, cc, firstMatcher), a, C, C, b);
        assertEquals(Sequences.insertAfter(seq, cc, lastMatcher), a, b, C, C);
        assertEquals(Sequences.insertAfter(seq, cc, allMatcher), a, C, C, b, C, C);
        assertEquals(Sequences.insertBefore(seq, cc, nullMatcher), seq);
        assertEquals(Sequences.insertBefore(seq, cc, firstMatcher), C, C, a, b);
        assertEquals(Sequences.insertBefore(seq, cc, lastMatcher), a, C, C, b);
        assertEquals(Sequences.insertBefore(seq, cc, allMatcher), C, C, a, C, C, b);

        assertTwoElements(Sequences.reverse(seq), b, a);
        assertEquals(seq.flatten(), a, b);
        assertDepth(0, seq.flatten());
    }


    /**
     * Generate empty sequences as many ways as we can think of and test their emptiness
     */
    public void testEmptySequence() {
        emptyHelper(new ArraySequence<Integer>(TypeInfos.Integer, EMPTY_SEQUENCE));
        emptyHelper(new ArraySequence<Integer>(TypeInfos.Integer, new Integer[0]));

        emptyHelper(Sequences.rangeExclusive(0, 0));

        emptyHelper(new CompositeSequence<Integer>(TypeInfos.Integer));
        emptyHelper(new CompositeSequence<Integer>(TypeInfos.Integer, EMPTY_SEQUENCE));
        emptyHelper(new CompositeSequence<Integer>(TypeInfos.Integer, EMPTY_SEQUENCE, EMPTY_SEQUENCE));
        emptyHelper(Sequences.concatenate(TypeInfos.Integer));
        emptyHelper(Sequences.concatenate(TypeInfos.Integer, EMPTY_SEQUENCE));
        emptyHelper(Sequences.concatenate(TypeInfos.Integer, EMPTY_SEQUENCE, EMPTY_SEQUENCE));

        emptyHelper(new FilterSequence<Integer>(EMPTY_SEQUENCE, new BitSet()));
        emptyHelper(new FilterSequence<Integer>(ONE_SEQUENCE, new BitSet()));
        emptyHelper(Sequences.filter(EMPTY_SEQUENCE, new BitSet()));
        emptyHelper(Sequences.filter(ONE_SEQUENCE, new BitSet()));
    }

    /**
     * Generate single-element sequences as many ways as we can think of and test their singularity
     */
    public void testOneElementSequence() {
        oneElementHelper(new ArraySequence<Integer>(TypeInfos.Integer, 1), 1);

        oneElementHelper(new SingletonSequence<Integer>(TypeInfos.Integer, 3), 3);

        oneElementHelper(new IntRangeSequence(2, 2), 2);
        oneElementHelper(Sequences.range(2, 2), 2);

        oneElementHelper(new CompositeSequence<Integer>(TypeInfos.Integer, EMPTY_SEQUENCE, ONE_SEQUENCE), A);
        oneElementHelper(new CompositeSequence<Integer>(TypeInfos.Integer, ONE_SEQUENCE, EMPTY_SEQUENCE), A);
        oneElementHelper(Sequences.concatenate(TypeInfos.Integer, EMPTY_SEQUENCE, ONE_SEQUENCE), A);
        oneElementHelper(Sequences.concatenate(TypeInfos.Integer, ONE_SEQUENCE, EMPTY_SEQUENCE), A);

        BitSet oneBit = new BitSet();
        oneBit.set(0);
        oneElementHelper(new FilterSequence<Integer>(ONE_SEQUENCE, oneBit), A);
        oneElementHelper(Sequences.filter(ONE_SEQUENCE, oneBit), A);
    }

    /**
     * Generate two-element sequences as many ways as we can think of and test their duality
     */
    public void testTwoElementSequence() {
        twoElementHelper(TWO_SEQUENCE, A, B);
        twoElementHelper(new CompositeSequence<Integer>(TypeInfos.Integer, ONE_SEQUENCE, ONE_SEQUENCE), A, A);
        twoElementHelper(new CompositeSequence<Integer>(TypeInfos.Integer, ONE_SEQUENCE, EMPTY_SEQUENCE, ONE_SEQUENCE), A, A);
        twoElementHelper(Sequences.concatenate(TypeInfos.Integer, ONE_SEQUENCE, ONE_SEQUENCE), A, A);
        twoElementHelper(Sequences.concatenate(TypeInfos.Integer, ONE_SEQUENCE, EMPTY_SEQUENCE, ONE_SEQUENCE), A, A);
        twoElementHelper(new IntRangeSequence(1, 2), 1, 2);
        twoElementHelper(Sequences.subsequence(new IntRangeSequence(1, 10), 3, 5), 4, 5);
        twoElementHelper(Sequences.subsequence(new IntRangeSequence(1, 10), 3, 7).get(oddMatcher), 5, 7);
    }

    /**
     * Create longer sequences in various ways and test their contents
     */
    public void testBiggerSequences() {
        assertEquals(TWO_SEQUENCE, A, B);
        assertEquals(Sequences.concatenate(TypeInfos.Integer, TWO_SEQUENCE, TWO_SEQUENCE), A, B, A, B);
        assertEquals(Sequences.concatenate(TypeInfos.Integer, TWO_SEQUENCE, EMPTY_SEQUENCE, TWO_SEQUENCE), A, B, A, B);
        assertEquals(Sequences.concatenate(TypeInfos.Integer, TWO_SEQUENCE, TWO_SEQUENCE, TWO_SEQUENCE), A, B, A, B, A, B);

        Sequence<Integer> five = Sequences.range(0, 5);
        assertEquals(Sequences.insertBefore(five, C, allMatcher), C, 0, C, 1, C, 2, C, 3, C, 4, C, 5);
        assertEquals(Sequences.insertAfter(five, C, allMatcher), 0, C, 1, C, 2, C, 3, C, 4, C, 5, C);

        assertEquals(Sequences.reverse(five), 5, 4, 3, 2, 1, 0);
        assertEquals(Sequences.reverse(Sequences.reverse(five)), five);

        assertEquals(Sequences.insertAfter(five, C, evenMatcher), 0, C, 1, 2, C, 3, 4, C, 5);
        assertEquals(Sequences.insertBefore(five, C, evenMatcher), C, 0, 1, C, 2, 3, C, 4, 5);

        assertEquals(Sequences.insertAfter(five, C, oddMatcher), 0, 1, C, 2, 3, C, 4, 5, C);
        assertEquals(Sequences.insertBefore(five, C, oddMatcher), 0, C, 1, 2, C, 3, 4, C, 5);
    }

    /**
     * Test extraction by predicate
     */
    public void testPredicateExtract() {
        Sequence<Integer> ten = Sequences.range(0, 10);
        assertOneElement(ten.get(firstMatcher), 0);
        assertOneElement(ten.get(lastMatcher), 10);
        assertEquals(ten.get(oddMatcher), 1, 3, 5, 7, 9);
        assertEquals(ten.get(oddMatcher).get(oddMatcher), 1, 3, 5, 7, 9);
        assertEquals(ten.get(evenMatcher), 0, 2, 4, 6, 8, 10);
        assertEquals(ten.get(evenMatcher).get(evenMatcher), 0, 2, 4, 6, 8, 10);
        assertEmpty(ten.get(evenMatcher).get(oddMatcher));
    }

    /**
     * Test various arguments to subsequence()
     */
    public void testSubsequence() {
        assertEmpty(Sequences.subsequence(EMPTY_SEQUENCE, 0, 10));

        Sequence<Integer> ten = Sequences.range(0, 10);
        assertEmpty(Sequences.subsequence(ten, -1, 0));
        assertEmpty(Sequences.subsequence(ten, -1, -1));
        assertEmpty(Sequences.subsequence(ten, 0, 0));
        assertEmpty(Sequences.subsequence(ten, 1, 1));
        assertEmpty(Sequences.subsequence(ten, 10, 10));
        assertEmpty(Sequences.subsequence(ten, 11, 12));
        assertEmpty(Sequences.subsequence(ten, 20, 30));

        assertOneElement(Sequences.subsequence(ten, 0, 1), 0);
        assertOneElement(Sequences.subsequence(ten, 10, 11), 10);
        assertOneElement(Sequences.subsequence(ten, 2, 3), 2);
        assertOneElement(Sequences.subsequence(ten, -5, 1), 0);
        assertOneElement(Sequences.subsequence(ten, 10, 33), 10);

        assertTwoElements(Sequences.subsequence(ten, 0, 2), 0, 1);
        assertTwoElements(Sequences.subsequence(ten, -1, 2), 0, 1);
        assertTwoElements(Sequences.subsequence(ten, 9, 11), 9, 10);

        assertEquals(Sequences.subsequence(ten, 0, 11), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(Sequences.subsequence(ten, 0, 10), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(Sequences.subsequence(ten, 1, 9), 1, 2, 3, 4, 5, 6, 7, 8);
    }

    /**
     * Test setting individual elements
     */
    public void testSet() {
        assertEquals(Sequences.set(TWO_SEQUENCE, 0, C), C, B);
        assertEquals(Sequences.set(TWO_SEQUENCE, 1, C), A, C);
        Sequence<Integer> five = Sequences.range(0, 5);
        assertEquals(Sequences.set(five, -1, C), 0, 1, 2, 3, 4, 5);
        assertEquals(Sequences.set(five, 0, C), C, 1, 2, 3, 4, 5);
        assertEquals(Sequences.set(five, 1, C), 0, C, 2, 3, 4, 5);
        assertEquals(Sequences.set(five, 2, C), 0, 1, C, 3, 4, 5);
        assertEquals(Sequences.set(five, 3, C), 0, 1, 2, C, 4, 5);
        assertEquals(Sequences.set(five, 4, C), 0, 1, 2, 3, C, 5);
        assertEquals(Sequences.set(five, 5, C), 0, 1, 2, 3, 4, C);
        assertEquals(Sequences.set(five, 6, C), 0, 1, 2, 3, 4, 5);
        assertEquals(Sequences.set(five, 0, null), 1, 2, 3, 4, 5);  // set(null) => delete
        assertEquals(Sequences.set(five, -1, null), 0, 1, 2, 3, 4, 5);  // set(null) => delete
        assertEquals(Sequences.set(five, 6, null), 0, 1, 2, 3, 4, 5);  // set(null) => delete
    }

    /**
     * Test ranges, including skip ranges and backwards ranges
     */
    public void testRange() {
        // [ 0..0 ] => [ 0 ]
        assertEquals(Sequences.range(0, 0), 0);
        // [ 0..<0 ] => [ 0 ]
        assertEquals(Sequences.rangeExclusive(0, 0), EMPTY_SEQUENCE);
        // [ 0..-1 ] => [ ]
        assertEquals(Sequences.range(0, -1));
        // [ 0..-1 STEP -1 ] => [ 0, -1 ]
        assertEquals(Sequences.range(0, -1, -1), 0, -1);
        // [ 0..0 STEP 3 ] => [ 0 ]
        assertEquals(Sequences.range(0, 0, 3), 0);
        // [ 0..<0 STEP 3 ] => [ 0 ]
        assertEquals(Sequences.rangeExclusive(0, 0, 3), EMPTY_SEQUENCE);
        // [ 0..1 ] => [ 0, 1 ]
        assertEquals(Sequences.range(0, 1), 0, 1);
        // [ 0..<1 ] => [ 0 ]
        assertEquals(Sequences.rangeExclusive(0, 1), 0);
        // [ 0..1 STEP 2 ] => [ 0 ]
        assertEquals(Sequences.range(0, 1, 2), 0);
        // [ 1..3 STEP 2 ] => [ 1, 3 ]
        assertEquals(Sequences.range(1, 3, 2), 1, 3);
        // [ 1..<3 STEP 2 ] => [ 1 ]
        assertEquals(Sequences.rangeExclusive(1, 3, 2), 1);
        // [ 1..4 STEP 2 ] => [ 1, 3 ]
        assertEquals(Sequences.range(1, 4, 2), 1, 3);
        // [ 1..<4 STEP 2 ] => [ 1, 3 ]
        assertEquals(Sequences.rangeExclusive(1, 4, 2), 1, 3);

        // [ 5..3 ] => [ 5, 4, 3 ]
        assertEquals(Sequences.range(5, 3, -1), 5, 4, 3);
        // [ 5..>3 ] => [ 5, 4 ]
        assertEquals(Sequences.rangeExclusive(5, 3, -1), 5, 4);
        // [ 5..3 STEP 2 ] => [ 5, 3 ]
        assertEquals(Sequences.range(5, 3, -2), 5, 3);
        // [ 5..>3 STEP 2 ] => [ 5 ]
        assertEquals(Sequences.rangeExclusive(5, 3, -2), 5);
        // [ 5..2 STEP 2 ] => [ 5, 3 ]
        assertEquals(Sequences.range(5, 2, -2), 5, 3);
        // [ 5..>2 STEP 2 ] => [ 5, 3 ]
        assertEquals(Sequences.rangeExclusive(5, 2, -2), 5, 3);
    }

    /**
     * Test out-of-bounds sets and gets
     */
    public void testOutOfBounds() {
        assertEquals(Integer.valueOf(0), EMPTY_SEQUENCE.get(-1));
        assertEquals(Sequences.set(EMPTY_SEQUENCE, 0, 1), EMPTY_SEQUENCE);
        assertEquals(Integer.valueOf(0), TWO_SEQUENCE.get(-1));
        assertEquals(Integer.valueOf(0), TWO_SEQUENCE.get(100));
        assertEquals(Sequences.set(TWO_SEQUENCE, -1, 400), TWO_SEQUENCE);
        assertEquals(Sequences.set(TWO_SEQUENCE, 200, 400), TWO_SEQUENCE);
    }

    public void testInsert() {
        Sequence<Integer> three = Sequences.range(1, 3);
        assertEquals(Sequences.insert(three, 4), 1, 2, 3, 4);
        assertEquals(Sequences.insertFirst(three, 0), 0, 1, 2, 3);
        assertEquals(Sequences.insertAfter(three, 0, -1), 1, 2, 3);
        assertEquals(Sequences.insertAfter(three, 0, 0), 1, 0, 2, 3);
        assertEquals(Sequences.insertAfter(three, 0, 1), 1, 2, 0, 3);
        assertEquals(Sequences.insertAfter(three, 0, 2), 1, 2, 3, 0);
        assertEquals(Sequences.insertAfter(three, 0, 3), 1, 2, 3);
        assertEquals(Sequences.insertBefore(three, 0, -1), 1, 2, 3);
        assertEquals(Sequences.insertBefore(three, 0, 0), 0, 1, 2, 3);
        assertEquals(Sequences.insertBefore(three, 0, 1), 1, 0, 2, 3);
        assertEquals(Sequences.insertBefore(three, 0, 2), 1, 2, 0, 3);
        assertEquals(Sequences.insertBefore(three, 0, 3), 1, 2, 3, 0);
        assertEquals(Sequences.insertAfter(three, 0, oneMatcher), 1, 0, 2, 3);
        assertEquals(Sequences.insertBefore(three, 0, oneMatcher), 0, 1, 2, 3);
        assertEquals(Sequences.insertBefore(three, 0, allMatcher), 0, 1, 0, 2, 0, 3);
        assertEquals(Sequences.insertAfter(three, 0, allMatcher), 1, 0, 2, 0, 3, 0);
    }

    public void testDelete() {
        Sequence<Integer> three = Sequences.range(1, 3);
        assertEquals(Sequences.delete(three, -1), 1, 2, 3);
        assertEquals(Sequences.delete(three, 0), 2, 3);
        assertEquals(Sequences.delete(three, 1), 1, 3);
        assertEquals(Sequences.delete(three, 2), 1, 2);
        assertEquals(Sequences.delete(three, 3), 1, 2, 3);
        assertEquals(Sequences.delete(three, 4), 1, 2, 3);
        assertEquals(Sequences.delete(three, firstMatcher), 2, 3);
        assertEquals(Sequences.delete(three, lastMatcher), 1, 2);
        assertEquals(Sequences.delete(three, oneMatcher), 2, 3);
        assertEquals(Sequences.delete(three, evenMatcher), 1, 3);
        assertEquals(Sequences.delete(three, oddMatcher), 2);
    }

    public void testGetSlices() {
        Sequence<Integer> three = Sequences.range(1, 3);
        assertEquals(three.getSlice(-1, -1));
        assertEquals(three.getSlice(-1, 0), 1);
        assertEquals(three.getSlice(0, -1));
        assertEquals(three.getSlice(1, 0));
        assertEquals(three.getSlice(2, 1));
        assertEquals(three.getSlice(0, 0), 1);
        assertEquals(three.getSlice(1, 1), 2);
        assertEquals(three.getSlice(2, 2), 3);
        assertEquals(three.getSlice(3, 3));
        assertEquals(three.getSlice(0, 1), 1, 2);
        assertEquals(three.getSlice(0, 2), 1, 2, 3);
        assertEquals(three.getSlice(0, 3), 1, 2, 3);
        assertEquals(three.getSlice(1, 2), 2, 3);
        assertEquals(three.getSlice(1, 3), 2, 3);
    }

    public void testSetSlices() {
        Sequence<Integer> three = Sequences.range(1, 3);
        Sequence<Integer> x = Sequences.make(TypeInfos.Integer, 0);
        assertEquals(Sequences.replaceSlice(three, 0, 0, x), 0, 2, 3);
        assertEquals(Sequences.replaceSlice(three, 1, 1, x), 1, 0, 3);
        assertEquals(Sequences.replaceSlice(three, 2, 2, x), 1, 2, 0);
        assertEquals(Sequences.replaceSlice(three, 3, 3, x), 1, 2, 3, 0);
        assertEquals(Sequences.replaceSlice(three, -1, -1, x), three);
        assertEquals(Sequences.replaceSlice(three, -1, 0, x), 1, 2, 3);
        assertEquals(Sequences.replaceSlice(three, 0, 1, x), 0, 3);
        assertEquals(Sequences.replaceSlice(three, 1, 2, x), 1, 0);
        assertEquals(Sequences.replaceSlice(three, 2, 3, x), 1, 2, 0);
        assertEquals(Sequences.replaceSlice(three, 0, 2, x), 0);
        assertEquals(Sequences.replaceSlice(three, 0, 3, x), 0);
        assertEquals(Sequences.replaceSlice(three, 0, 4, x), 0);
    }

    /**
     * Tests properties of the SequenceHelper methods, which optimize certain common cases such as concatenating an
     * empty sequence, or extracting the entire sequence (or an empty sequence) using subsequence, or filtering
     * a sequence where all or none of the bits are set.
     */
    public void testDepths() {
        assertDepth(0, EMPTY_SEQUENCE);
        assertDepth(0, ONE_SEQUENCE);
        assertDepth(0, new SingletonSequence<Integer>(TypeInfos.Integer, 1));
        assertDepth(0, new ArraySequence<Integer>(TypeInfos.Integer, 1));
        assertDepth(0, new IntRangeSequence(0, 1));
        assertDepth(1, new CompositeSequence<Integer>(TypeInfos.Integer));
        assertDepth(1, new CompositeSequence<Integer>(TypeInfos.Integer, EMPTY_SEQUENCE));
        assertDepth(1, new CompositeSequence<Integer>(TypeInfos.Integer, ONE_SEQUENCE, ONE_SEQUENCE));
        assertDepth(1, new FilterSequence<Integer>(EMPTY_SEQUENCE, new BitSet()));
        assertDepth(1, new FilterSequence<Integer>(ONE_SEQUENCE, new BitSet()));

        assertDepth(1, new CompositeSequence<Integer>(TypeInfos.Integer, ONE_SEQUENCE, ONE_SEQUENCE));
        assertDepth(0, Sequences.concatenate(TypeInfos.Integer, ONE_SEQUENCE, ONE_SEQUENCE));
        assertDepth(0, Sequences.concatenate(TypeInfos.Integer, EMPTY_SEQUENCE, ONE_SEQUENCE));
        assertDepth(0, Sequences.concatenate(TypeInfos.Integer, ONE_SEQUENCE, EMPTY_SEQUENCE));

        assertDepth(1, new FilterSequence<Integer>(ONE_SEQUENCE, ONE_SEQUENCE.getBits(nullMatcher)));
        assertDepth(1, new FilterSequence<Integer>(ONE_SEQUENCE, ONE_SEQUENCE.getBits(allMatcher)));
        assertDepth(0, Sequences.filter(ONE_SEQUENCE, ONE_SEQUENCE.getBits(nullMatcher)));
        assertDepth(0, Sequences.filter(ONE_SEQUENCE, ONE_SEQUENCE.getBits(allMatcher)));
        assertDepth(0, Sequences.filter(TWO_SEQUENCE, TWO_SEQUENCE.getBits(nullMatcher)));
        assertDepth(0, Sequences.filter(TWO_SEQUENCE, TWO_SEQUENCE.getBits(allMatcher)));
        assertDepth(1, Sequences.filter(TWO_SEQUENCE, TWO_SEQUENCE.getBits(firstMatcher)));

        assertDepth(1, new SubSequence<Integer>(TWO_SEQUENCE, 0, 2));
        assertDepth(1, new SubSequence<Integer>(TWO_SEQUENCE, 0, 0));
        assertDepth(0, Sequences.subsequence(TWO_SEQUENCE, 0, 2));
        assertDepth(1, Sequences.subsequence(TWO_SEQUENCE, 1, 2));
        assertDepth(0, Sequences.subsequence(TWO_SEQUENCE, 0, 0));
        assertDepth(1, Sequences.subsequence(TWO_SEQUENCE, 0, 1));

        assertDepth(1, Sequences.reverse(TWO_SEQUENCE));
    }

    public void testUpcast() {
        Sequence<Integer> seq = Sequences.range(1, 5);
        Sequence<Number> asNumber = Sequences.<Number>upcast(seq);
        assertEquals(asNumber, 1, 2, 3, 4, 5);

        Sequence<Number> addSix = Sequences.insert(asNumber, 6.0);
        assertEquals(addSix, 1, 2, 3, 4, 5, 6.0);
    }

    public void testOverflow() {
        assertThrows(IllegalArgumentException.class, new VoidCallable() {
            public void call() throws Exception {
                Sequence<Integer> seq = Sequences.range(Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
        });
        assertThrows(IllegalArgumentException.class, new VoidCallable() {
            public void call() throws Exception {
                Sequence<Integer> seq = Sequences.range(Integer.MIN_VALUE, 0);
            }
        });
        assertThrows(IllegalArgumentException.class, new VoidCallable() {
            public void call() throws Exception {
                Sequence<Integer> seq = Sequences.range(0, Integer.MAX_VALUE);
            }
        });
        Sequence<Integer> seq = Sequences.range(1, Integer.MAX_VALUE);
        assertEquals(seq.size(), Integer.MAX_VALUE);
        seq = Sequences.range(Integer.MIN_VALUE + 2, 0);
        assertEquals(seq.size(), Integer.MAX_VALUE);
    }
}
