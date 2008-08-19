package com.sun.javafx.runtime.location;

import com.sun.javafx.functions.Function0;
import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * BoundOperatorsTest
 *
 * @author Brian Goetz
 */
public class BoundOperatorsTest extends JavaFXTestCase {

    public void testIndirectIf() {
        BooleanLocation b = BooleanVariable.make(true);
        final IntVariable i = IntVariable.make(1);
        IntLocation ifLoc = BoundOperators.makeBoundIf(false, b,
                                                       new Function0<IntLocation>() {
                                                           public IntLocation invoke() {
                                                               return IntVariable.make(new IntBindingExpression() {
                                                                   public int computeValue() {
                                                                       return i.get();
                                                                   }
                                                               }, i);
                                                           }
                                                       },
                                                       new Function0<IntLocation>() {
                                                           public IntLocation invoke() {
                                                               return IntVariable.make(new IntBindingExpression() {
                                                                   public int computeValue() {
                                                                       return -i.get();
                                                                   }
                                                               }, i);
                                                           }
                                                       });
        CountingListener cl = new CountingListener();
        ifLoc.addChangeListener(cl);
        assertEquals(1, ifLoc.getAsInt());
        assertEquals(0, cl.count);

        b.set(true);
        assertEquals(1, ifLoc.getAsInt());
        assertEquals(0, cl.count);

        b.set(false);
        assertEquals(-1, ifLoc.getAsInt());
        assertEquals(1, cl.count);

        i.set(3);
        assertEquals(-3, ifLoc.getAsInt());
        assertEquals(2, cl.count);

        i.set(3);
        assertEquals(-3, ifLoc.getAsInt());
        assertEquals(2, cl.count);

        b.set(true);
        assertEquals(3, ifLoc.getAsInt());
        assertEquals(3, cl.count);

        i.set(3);
        assertEquals(3, ifLoc.getAsInt());
        assertEquals(3, cl.count);

        i.set(5);
        assertEquals(5, ifLoc.getAsInt());
        assertEquals(4, cl.count);
    }

    public void testIndirectSeqIf() {
        BooleanLocation b = BooleanVariable.make(true);
        final SequenceVariable<Integer> x = SequenceVariable.make(Integer.class, Sequences.make(Integer.class, 1, 2, 3));
        final SequenceVariable<Integer> y = SequenceVariable.make(Integer.class, Sequences.make(Integer.class, 4, 5, 6));
        SequenceLocation<Integer> ifLoc = BoundOperators.makeBoundIf(Integer.class, false, b,
                                                                     new Function0<SequenceLocation<Integer>>() {
                                                                         public SequenceLocation<Integer> invoke() {
                                                                             return x;
                                                                         }
                                                                     },
                                                                     new Function0<SequenceLocation<Integer>>() {
                                                                         public SequenceLocation<Integer> invoke() {
                                                                             return y;
                                                                         }
                                                                     });
        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        ifLoc.addChangeListener(hl);
        assertEqualsAndClear(hl);
        assertEquals(ifLoc.getAsSequence(), 1, 2, 3);

        x.insert(4);
        assertEquals(ifLoc.getAsSequence(), 1, 2, 3, 4);
        assertEqualsAndClear(hl, "[3, 2] => [ 4 ]");

        b.set(false);
        assertEquals(ifLoc.getAsSequence(), 4, 5, 6);
        assertEqualsAndClear(hl, "[0, 3] => [ 4, 5, 6 ]");

        x.insert(5);
        assertEquals(ifLoc.getAsSequence(), 4, 5, 6);
        assertEqualsAndClear(hl);

        y.insert(7);
        assertEquals(ifLoc.getAsSequence(), 4, 5, 6, 7);
        assertEqualsAndClear(hl, "[3, 2] => [ 7 ]");

        b.set(true);
        assertEquals(ifLoc.getAsSequence(), 1, 2, 3, 4, 5);
        assertEqualsAndClear(hl, "[0, 3] => [ 1, 2, 3, 4, 5 ]");
    }

}
