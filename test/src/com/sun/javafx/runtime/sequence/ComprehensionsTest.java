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
        Sequence<Integer> five = Sequences.rangeSequence(0, 5);
        SequenceMapper<Integer, Integer> doubler = new SequenceMapper<Integer, Integer>() {
            public Integer map(Sequence<Integer> sequence, int index, Integer value) {
                return value*2;
            }
        };
        assertEquals(five.map(Integer.class, doubler), 0, 2, 4, 6, 8, 10);
    }

    /** select x from foo where x > 3
     *  select x from foo where indexof x % 2 != 0
     */
    public void testSimpleSelect() {
        Sequence<Integer> five = Sequences.rangeSequence(1, 5);
        Sequence<Integer> six = Sequences.rangeSequence(1, 6);
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
}
