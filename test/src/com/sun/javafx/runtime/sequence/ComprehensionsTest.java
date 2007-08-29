package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.JavaFXTestCase;

/**
 * ComprehensionsTest
 *
 * @author Brian Goetz
 */
public class ComprehensionsTest extends JavaFXTestCase {

    /**  foreach (i in sequence) { 2*i } */
    public void testSimpleForeach() {
        Sequence<Integer> five = Sequences.range(0, 5);
        SequenceMapper<Integer, Integer> doubler = new SequenceMapper<Integer, Integer>() {
            public Integer map(Sequence<Integer> sequence, int index, Integer value) {
                return value*2;
            }
        };
        Sequence<Integer> doubled = five.map(Integer.class, doubler);
        assertEquals(doubled, 0, 2, 4, 6, 8, 10);
        assertEquals(doubled, doubled.flatten());
    }

    /** select x from foo where x > 3
     *  select x from foo where indexof x % 2 != 0
     */
    public void testSimpleSelect() {
        Sequence<Integer> five = Sequences.range(1, 5);
        Sequence<Integer> six = Sequences.range(1, 6);
        SequencePredicate<Integer> greaterThanThree = new SequencePredicate<Integer>() {
            public boolean matches(Sequence<Integer> sequence, int index, Integer value) {
                return value > 3;
            }
        };
        SequencePredicate<Integer> oddIndex = new SequencePredicate<Integer>() {
            public boolean matches(Sequence<Integer> sequence, int index, Integer value) {
                return index % 2 != 0;
            }
        };
        assertEquals(five.get(greaterThanThree), 4, 5);
        assertEquals(six.get(greaterThanThree), 4, 5, 6);
        assertEquals(five.get(oddIndex), 2, 4);
        assertEquals(six.get(oddIndex), 2, 4, 6);
    }

    /** foreach (i in outer) foreach (j in inner) { content } */
    public void testNestedForeach() {
        // outer = [ 1..2 ], inner = [ 1..3 ], content = [ 1 ]
        Sequence<Integer> outer = Sequences.range(1, 2);
        Sequence<Integer> inner = Sequences.range(1, 3);
        SequenceBuilder<Integer> sb = new SequenceBuilder<Integer>(Integer.class, outer.size() * inner.size());
        for (Integer i : outer) {
            for (Integer j : inner) {
                sb.add(1);
            }
        }
        Sequence<Integer> result = sb.toSequence();
        assertEquals(result, 1, 1, 1, 1, 1, 1);

        // outer = [ 1..2 ], inner = [ 1..3 ], content = [ i*j ]
        outer = Sequences.range(1, 2);
        inner = Sequences.range(1, 3);
        sb = new SequenceBuilder<Integer>(Integer.class, outer.size() * inner.size());
        for (Integer i : outer) {
            for (Integer j : inner) {
                sb.add(i*j);
            }
        }
        result = sb.toSequence();
        assertEquals(result, 1, 2, 3, 2, 4, 6);
    }
}
