package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;
import com.sun.javafx.runtime.sequence.Sequences;
import com.sun.javafx.runtime.JavaFXTestCase;

/**
 * SequenceBindingTest
 *
 * @author Brian Goetz
 */
public class SequenceBindingTest extends JavaFXTestCase {

    private final SequencePredicate<Integer> isOnePredicate = new SequencePredicate<Integer>() {
        public boolean matches(Sequence sequence, int index, Integer value) {
            return value == 1;
        }
    };


    public void testUnbound() {
        Sequence<Integer> seq = Sequences.range(1, 100);
        SequenceVar<Integer> loc = SequenceVar.make(seq);
        assertEquals(seq, loc.get());
    }

    /** bind first = seq[0] */
    public void testElementBind() {
        final SequenceVar<Integer> seq = SequenceVar.make(Sequences.range(1, 3));
        IntLocation firstValue = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return seq.get().get(0);
            }
        }, seq);
        CountingSequenceListener cl = new CountingSequenceListener();
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
        final SequenceVar<Integer> seq = SequenceVar.make(Sequences.range(1, 3));
        CountingSequenceListener cl = new CountingSequenceListener();
        seq.addChangeListener(cl);

        seq.insert(4);
        assertEquals(seq, 1, 2, 3, 4);
        assertEquals(1, cl.insertCount);
        assertEquals(cl.inserted, 4);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.insert(Sequences.range(1, 3));
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

        seq.insertAfter(Sequences.range(1, 3), 9);
        assertEquals(seq, 1, 8, 2, 3, 4, 1, 8, 2, 10, 3, 1, 2, 3);
        assertEquals(10, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.insertAfter(Sequences.range(1, 3), isOnePredicate);
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

        seq.insertBefore(Sequences.range(1, 3), 10);
        assertEquals(seq, 9, 1, 9, 1, 2, 3, 8, 10, 2, 3, 1, 2, 3, 4, 9, 1, 9, 1, 2, 3, 8, 2, 10, 3, 9, 1, 9, 1, 2, 3, 2, 3);
        assertEquals(29, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 9, 9, 9, 9, 9, 9, 1, 2, 3);
        assertEquals(cl.changeCount, cl.insertCount);

        seq.insertBefore(Sequences.range(1, 3), new SequencePredicate<Integer>() {
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

        seq.insertFirst(Sequences.range(33, 34));
        assertEquals(seq, 33, 34, 777, 9, 3, 8, 2, 3, 10, 2, 3, 2, 3, 4, 9, 9, 2, 3, 8, 2, 2, 3, 10, 3, 9, 9, 2, 3, 2, 3);
        assertEquals(38, cl.insertCount);
        assertEquals(cl.inserted, 4, 1, 2, 3, 10, 8, 8, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 10, 9, 9, 9, 9, 9, 9, 1, 2, 3, 1, 2, 3, 1, 2, 3, 777, 33, 34);
        assertEquals(cl.changeCount, cl.insertCount + cl.deleteCount);
    }

    public void testSequenceExpression() {
        // oneToN = bind 1..n
        final IntLocation n = IntVar.make(0);
        final SequenceLocation<Integer> oneToN = SequenceExpression.make(new SequenceBindingExpression<Integer>() {
            public Sequence<Integer> get() {
                return Sequences.range(1, n.get());
            }
        }, n);
        assertEquals(oneToN.get(), Sequences.emptySequence(Integer.class));
        n.set(3);
        assertEquals(oneToN.get(), 1, 2, 3);
        n.set(1);
        assertEquals(oneToN.get(), 1);
        n.set(-1);
        assertEquals(oneToN.get(), Sequences.emptySequence(Integer.class));

        // oddN = bind select t from v where t % 2 == 1
        final SequenceLocation<Integer> v = SequenceVar.make(Sequences.range(1, 8));
        final SequenceLocation<Integer> oddN = SequenceExpression.make(new SequenceBindingExpression<Integer>() {
            public Sequence<Integer> get() {
                return v.get().get(new SequencePredicate<Integer>() {
                    public boolean matches(Sequence<Integer> sequence, int index, Integer value) {
                        return value % 2 == 1;
                    }
                });
            }
        }, v);
        assertEquals(oddN.get(), 1, 3, 5, 7);
        v.set(Sequences.range(3, 11));
        assertEquals(oddN.get(), 3, 5, 7, 9, 11);
    }

    public void testNestedSequenceBinding() {
        // oneToN = bind 1..n
        // evenN = bind select x from oneToN where x % 2 == 0
        final IntLocation n = IntVar.make(0);
        final SequenceLocation<Integer> oneToN = SequenceExpression.make(new SequenceBindingExpression<Integer>() {
            public Sequence<Integer> get() {
                return Sequences.range(1, n.get());
            }
        }, n);
        final SequenceLocation<Integer> evenN = SequenceExpression.make(new SequenceBindingExpression<Integer>() {
            public Sequence<Integer> get() {
                return oneToN.get().get(new SequencePredicate<Integer>() {
                    public boolean matches(Sequence<Integer> sequence, int index, Integer value) {
                        return value % 2 == 0;
                    }
                });
            }
        }, oneToN);
        assertEquals(evenN.get(), Sequences.emptySequence(Integer.class));
        n.set(3);
        assertEquals(evenN.get(), 2);
        n.set(11);
        assertEquals(evenN.get(), 2, 4, 6, 8, 10);
        n.set(-1);
        assertEquals(evenN.get(), Sequences.emptySequence(Integer.class));
    }
}

