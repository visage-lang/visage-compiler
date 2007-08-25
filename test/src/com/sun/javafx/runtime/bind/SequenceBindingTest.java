package com.sun.javafx.runtime.bind;

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.ArraySequence;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;
import com.sun.javafx.runtime.sequence.Sequences;
import junit.framework.TestCase;

/**
 * SequenceBindingTest
 *
 * @author Brian Goetz
 */
public class SequenceBindingTest extends TestCase {

    private static class CountingListener implements SequenceChangeListener<Integer> {
        int changeCount, insertCount, deleteCount, replaceCount;
        Sequence<Integer> inserted = Sequences.emptySequence(Integer.class);
        Sequence<Integer> deleted = Sequences.emptySequence(Integer.class);

        public void onChange() {
            ++changeCount;
        }

        public void onInsert(Integer element) {
            ++insertCount;
            inserted = inserted.insert(element);
        }

        public void onDelete(Integer element) {
            ++deleteCount;
            deleted = deleted.insert(element);
        }

        public void onReplace(int position, Integer oldValue, Integer newValue) {
            ++replaceCount;
        }
    }

    private void assertEquals(int value, IntLocation loc) {
        assertTrue(loc.isValid());
        assertEquals(value, loc.get());
    }

    private void assertEqualsLazy(int value, IntLocation loc) {
        assertFalse(loc.isValid());
        assertEquals(value, loc.get());
        assertTrue(loc.isValid());
    }

    private <T> void assertEquals(Sequence<T> sequence, T value) {
        assertEquals(sequence, new ArraySequence<T>(sequence.getElementType(), value));
    }

    private <T> void assertEquals(Sequence<T> sequence, T... values) {
        assertEquals(sequence, new ArraySequence<T>(sequence.getElementType(), values));
    }

    private <T> void assertEquals(SequenceLocation<T> sequence, T... values) {
        assertEquals(sequence.get(), values);
    }

    private final SequencePredicate<Integer> isOnePredicate = new SequencePredicate<Integer>() {
        public boolean matches(Sequence sequence, int index, Integer value) {
            return value == 1;
        }
    };


    public void testUnbound() {
        Sequence<Integer> seq = Sequences.rangeSequence(1, 100);
        SequenceVar<Integer> loc = SequenceVar.make(seq);
        assertEquals(seq, loc.get());
    }

    public void testElementBind() {
        final SequenceVar<Integer> seq = SequenceVar.make(Sequences.rangeSequence(1, 3));
        IntLocation firstValue = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return seq.get().get(0);
            }
        }, seq);
        CountingListener cl = new CountingListener();
        seq.addChangeListener(cl);
        assertEquals(seq, 1, 2, 3);

        assertEqualsLazy(1, firstValue);
        seq.set(0, 3);
        assertEquals(seq, 3, 2, 3);
        assertEquals(3, firstValue);
        assertEquals(1, cl.changeCount);
        assertEquals(1, cl.replaceCount);
    }

    public void testSequenceListener() {
        final SequenceVar<Integer> seq = SequenceVar.make(Sequences.rangeSequence(1, 3));
        CountingListener cl = new CountingListener();
        seq.addChangeListener(cl);

        seq.insert(4);
        assertEquals(seq, 1, 2, 3, 4);
        assertEquals(1, cl.insertCount);
        assertEquals(cl.inserted, 4);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.insert(Sequences.rangeSequence(1, 3));
        assertEquals(seq, 1, 2, 3, 4, 1, 2, 3);
        assertEquals(4, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.insertAfter(10, 5);
        assertEquals(seq, 1, 2, 3, 4, 1, 2, 10, 3);
        assertEquals(5, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.insertAfter(8, isOnePredicate);
        assertEquals(seq, 1, 8, 2, 3, 4, 1, 8, 2, 10, 3);
        assertEquals(7, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.insertAfter(Sequences.rangeSequence(1, 3), 9);
        assertEquals(seq, 1, 8, 2, 3, 4, 1, 8, 2, 10, 3, 1, 2, 3);
        assertEquals(10, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.insertAfter(Sequences.rangeSequence(1, 3), isOnePredicate);
        assertEquals(seq, 1, 1, 2, 3, 8, 2, 3, 4, 1, 1, 2, 3, 8, 2, 10, 3, 1, 1, 2, 3, 2, 3);
        assertEquals(19, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.insertBefore(10, 5);
        assertEquals(seq, 1, 1, 2, 3, 8, 10, 2, 3, 4, 1, 1, 2, 3, 8, 2, 10, 3, 1, 1, 2, 3, 2, 3);
        assertEquals(20, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.insertBefore(9, isOnePredicate);
        assertEquals(seq, 9, 1, 9, 1, 2, 3, 8, 10, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(26, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 9, 9, 9, 9, 9, 9);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.insertBefore(Sequences.rangeSequence(1, 3), 10);
        assertEquals(seq, 9, 1, 9, 1, 2, 3, 8, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(29, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 9, 9, 9, 9, 9, 9, 1, 2, 3);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.insertBefore(Sequences.rangeSequence(1, 3), new SequencePredicate<Integer>() {
            public boolean matches(Sequence sequence, int index, Integer value) {
                return value == 10;
            }
        });
        assertEquals(seq, 9, 1, 9, 1, 2, 3, 8, 1, 2, 3, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 1, 2, 3, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(35, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 9, 9, 9, 9, 9, 9, 1, 2, 3, 1, 2, 3, 1, 2, 3);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.delete(0);
        assertEquals(seq, 1, 9, 1, 2, 3, 8, 1, 2, 3, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 1, 2, 3, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(1, cl.deleteCount);
        assertEquals(cl.deleted, 9);
        assertEquals(cl.changeCount, cl.insertCount + cl.deleteCount);

        seq.delete(-1); // no effect
        assertEquals(seq, 1, 9, 1, 2, 3, 8, 1, 2, 3, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 1, 2, 3, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(1, cl.deleteCount);
        assertEquals(cl.deleted, 9);
        assertEquals(cl.changeCount, cl.insertCount + cl.deleteCount);

        seq.delete(100); // no effect
        assertEquals(seq, 1, 9, 1, 2, 3, 8, 1, 2, 3, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 1, 2, 3, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(1, cl.deleteCount);
        assertEquals(cl.deleted, 9);
        assertEquals(cl.changeCount, cl.insertCount + cl.deleteCount);

        seq.delete(3);
        assertEquals(seq, 1, 9, 1, 3, 8, 1, 2, 3, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 1, 2, 3, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(2, cl.deleteCount);
        assertEquals(cl.deleted, 9, 2);
        assertEquals(cl.changeCount, cl.insertCount + cl.deleteCount);

        seq.delete(isOnePredicate);
        assertEquals(seq, 9, 3, 8, 2, 3, 10, 2, 3, 2, 3, 4, 9, 9, 2, 3, 8, 2, 2, 3, 10, 3, 9, 9, 2, 3, 2, 3);
        assertEquals(11, cl.deleteCount);
        assertEquals(cl.deleted, 9, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        assertEquals(cl.changeCount, cl.insertCount + cl.deleteCount);

        seq.insertFirst(777);
        assertEquals(seq, 777, 9, 3, 8, 2, 3, 10, 2, 3, 2, 3, 4, 9, 9, 2, 3, 8, 2, 2, 3, 10, 3, 9, 9, 2, 3, 2, 3);
        assertEquals(36, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 9, 9, 9, 9, 9, 9, 1, 2, 3, 1, 2, 3, 1, 2, 3, 777);
        assertEquals(cl.changeCount, cl.insertCount + cl.deleteCount);

        seq.insertFirst(Sequences.rangeSequence(33, 34));
        assertEquals(seq, 33, 34, 777, 9, 3, 8, 2, 3, 10, 2, 3, 2, 3, 4, 9, 9, 2, 3, 8, 2, 2, 3, 10, 3, 9, 9, 2, 3, 2, 3);
        assertEquals(38, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 9, 9, 9, 9, 9, 9, 1, 2, 3, 1, 2, 3, 1, 2, 3, 777, 33, 34);
        assertEquals(cl.changeCount, cl.insertCount + cl.deleteCount);

    }
}
