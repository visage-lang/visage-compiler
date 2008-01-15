package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.location.*;

/**
 * BoundComprehensionTest
 *
 * @author Brian Goetz
 */
public class BoundComprehensionTest extends JavaFXTestCase {

    public void testSimpleComprehension() {
        SequenceLocation<Integer> base = SequenceVar.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = new SimpleBoundComprehension<Integer, Integer>(Integer.class, base) {
            Integer computeElement(Integer element, int index) {
                return element * 2;
            }
        };
        IntLocation len = new IntExpression(false, derived) {
            protected int computeValue() {
                return Sequences.size(derived.getAsSequence());
            }
        };

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 2, 4, 6);
        assertEquals(3, len.getAsInt());

        base.insert(4);
        assertEquals(derived, 2, 4, 6, 8);
        assertEquals(4, len.getAsInt());
        assertEqualsAndClear(hl, "[3, 2] => [ 8 ]");

        base.delete(0);
        assertEquals(derived, 4, 6, 8);
        assertEquals(3, len.getAsInt());
        assertEqualsAndClear(hl, "[0, 0] => [ ]");

        base.set(0, 0);
        assertEquals(derived, 0, 6, 8);
        assertEquals(3, len.getAsInt());
        assertEqualsAndClear(hl, "[0, 0] => [ 0 ]");

        base.insertFirst(-1);
        assertEquals(derived, -2, 0, 6, 8);
        assertEquals(4, len.getAsInt());
        assertEqualsAndClear(hl, "[0, -1] => [ -2 ]");
    }

    public void testSimpleChainedComprehension() {
        SequenceLocation<Integer> base = SequenceVar.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> middle = new SimpleBoundComprehension<Integer, Integer>(Integer.class, base) {
            Integer computeElement(Integer element, int index) {
                return element * 2;
            }
        };
        final SequenceLocation<Integer> derived = new SimpleBoundComprehension<Integer, Integer>(Integer.class, middle) {
            Integer computeElement(Integer element, int index) {
                return element * 2;
            }
        };
        IntLocation len = new IntExpression(false, derived) {
            protected int computeValue() {
                return Sequences.size(derived.getAsSequence());
            }
        };

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 4, 8, 12);
        assertEquals(3, len.getAsInt());

        base.insert(4);
        assertEquals(derived, 4, 8, 12, 16);
        assertEquals(4, len.getAsInt());
        assertEqualsAndClear(hl, "[3, 2] => [ 16 ]");

        base.delete(0);
        assertEquals(derived, 8, 12, 16);
        assertEquals(3, len.getAsInt());
        assertEqualsAndClear(hl, "[0, 0] => [ ]");

        base.set(0, 0);
        assertEquals(derived, 0, 12, 16);
        assertEquals(3, len.getAsInt());
        assertEqualsAndClear(hl, "[0, 0] => [ 0 ]");

        base.insertFirst(-1);
        assertEquals(derived, -4, 0, 12, 16);
        assertEquals(4, len.getAsInt());
        assertEqualsAndClear(hl, "[0, -1] => [ -4 ]");
    }

    public void testSimpleIndex() {
        SequenceLocation<Integer> base = SequenceVar.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = new SimpleBoundComprehension<Integer, Integer>(Integer.class, base, true) {
            Integer computeElement(Integer element, int index) {
                return index * 10 + element;
            }
        };
        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 1, 12, 23);

        base.insert(4);
        assertEquals(derived, 1, 12, 23, 34);
        assertEqualsAndClear(hl, "[3, 2] => [ 34 ]");

        base.delete(0);
        assertEquals(derived, 2, 13, 24);
        assertEqualsAndClear(hl, "[0, 3] => [ 2, 13, 24 ]");

        base.insertFirst(-1);
        assertEquals(derived, -1, 12, 23, 34);
        assertEqualsAndClear(hl, "[0, 2] => [ -1, 12, 23, 34 ]");

        base.set(0, 0); // 0, 2, 3, 4
        assertEquals(derived, 0, 12, 23, 34);
        assertEqualsAndClear(hl, "[0, 0] => [ 0 ]");

        base.insertAfter(0, 1); // 0, 2, 0, 3, 4
        assertEquals(derived, 0, 12, 20, 33, 44);
        assertEqualsAndClear(hl, "[2, 3] => [ 20, 33, 44 ]");

    }
}
