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
        SequenceLocation<Integer> base = SequenceVariable.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = new SimpleBoundComprehension<Integer, Integer>(Integer.class, base) {
            protected Integer computeElement$(Integer element, int index) {
                return element * 2;
            }
        };
        IntLocation len = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return Sequences.size(derived.getAsSequence());
            }
        }, derived);

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 2, 4, 6);
        assertEqualsAndClear(hl, "[0, -1] => [ 2, 4, 6 ]");
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
        SequenceLocation<Integer> base = SequenceVariable.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> middle = new SimpleBoundComprehension<Integer, Integer>(Integer.class, base) {
            protected Integer computeElement$(Integer element, int index) {
                return element * 2;
            }
        };
        final SequenceLocation<Integer> derived = new SimpleBoundComprehension<Integer, Integer>(Integer.class, middle) {
            protected Integer computeElement$(Integer element, int index) {
                return element * 2;
            }
        };
        IntLocation len = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return Sequences.size(derived.getAsSequence());
            }
        }, derived);

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 4, 8, 12);
        assertEqualsAndClear(hl, "[0, -1] => [ 4, 8, 12 ]");
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
        SequenceLocation<Integer> base = SequenceVariable.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = new SimpleBoundComprehension<Integer, Integer>(Integer.class, base, true) {
            protected Integer computeElement$(Integer element, int index) {
                return index * 10 + element;
            }
        };
        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 1, 12, 23);
        assertEqualsAndClear(hl, "[0, -1] => [ 1, 12, 23 ]");

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

    public void testBoundComprehension() {
        SequenceLocation<Integer> base = SequenceVariable.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = new BoundComprehension<Integer, Integer>(Integer.class, base, false) {

            protected ComprehensionElement<Integer, Integer> processElement$(final Integer element, final int index) {
                ComprehensionElement<Integer, Integer> ce = new AbstractComprehensionElement<Integer, Integer>(Integer.class) {
                    private Integer x;
                    private int xIndex;

                    public void setElement(Integer element) {
                        x = element;
                    }

                    public void setIndex(int index) {
                        xIndex = index;
                    }

                    public Sequence<Integer> computeValue() {
                        return Sequences.make(Integer.class, x * 2);
                    }
                };
                ce.setElement(element);
                ce.setIndex(index);
                return ce;
            }
        };
        IntVariable len = IntVariable.make(false, new IntBindingExpression() {
            public int computeValue() {
                return Sequences.size(derived.getAsSequence());
            }
        }, derived);

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 2, 4, 6);
        assertEqualsAndClear(hl, "[0, -1] => [ 2, 4, 6 ]");
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

    public void testBindingWrapper() {
        SequenceLocation<Integer> base = SequenceVariable.make(Sequences.range(1, 3));
        final SequenceLocation<Integer> derived = new SimpleBoundComprehension<Integer, Integer>(Integer.class, base) {
            protected Integer computeElement$(Integer element, int index) {
                return element * 2;
            }
        };
        SequenceVariable<Integer> moo = SequenceVariable.make(Integer.class);
        moo.bind(derived);
        
        assertEquals(derived, 2, 4, 6);
        assertEquals(moo, 2, 4, 6);
        base.insert(4);
        assertEquals(derived, 2, 4, 6, 8);
        assertEquals(moo, 2, 4, 6, 8);
    }
}
