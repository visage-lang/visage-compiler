package com.sun.javafx.runtime.location;

import java.util.HashMap;
import java.util.Map;

import com.sun.javafx.functions.Function0;
import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.TypeInfo;
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
        IntLocation ifLoc = BoundOperators.makeBoundIf(TypeInfo.Integer, false, b,
                                                       IntVariable.make(true, new AbstractBindingExpression() {
                                                           public void compute() {
                                                               pushValue(i.getAsInt());
                                                           }
                                                       }, i),
                                                       IntVariable.make(true, new AbstractBindingExpression() {
                                                           public void compute() {
                                                               pushValue(-i.getAsInt());
                                                           }
                                                       }, i));
        IntLocation lazyIfLoc = BoundOperators.makeBoundIf(TypeInfo.Integer, true, b,
                                                           IntVariable.make(true, new AbstractBindingExpression() {
                                                               public void compute() {
                                                                   pushValue(i.getAsInt());
                                                               }
                                                           }, i),
                                                           IntVariable.make(true, new AbstractBindingExpression() {
                                                               public void compute() {
                                                                   pushValue(-i.getAsInt());
                                                               }
                                                           }, i));
        CountingListener cl = new CountingListener();
        ifLoc.addInvalidationListener(cl);
        assertEquals(1, ifLoc.getAsInt());
        assertEqualsLazy(1, lazyIfLoc);
        assertEquals(0, cl.count);

        b.set(true);
        assertEquals(1, ifLoc.getAsInt());
        assertEquals(1, lazyIfLoc.getAsInt());
        assertEquals(0, cl.count);

        b.set(false);
        assertEquals(-1, ifLoc.getAsInt());
        assertEqualsLazy(-1, lazyIfLoc);
        assertEquals(1, cl.count);

        i.set(3);
        assertEquals(-3, ifLoc.getAsInt());
        assertEqualsLazy(-3, lazyIfLoc);
        assertEquals(2, cl.count);

        i.set(3);
        assertEquals(-3, ifLoc.getAsInt());
        assertEquals(-3, lazyIfLoc.getAsInt());
        assertEquals(2, cl.count);

        b.set(true);
        assertEquals(3, ifLoc.getAsInt());
        assertEqualsLazy(3, lazyIfLoc);
        assertEquals(3, cl.count);

        i.set(3);
        assertEquals(3, ifLoc.getAsInt());
        assertEquals(3, lazyIfLoc.getAsInt());
        assertEquals(3, cl.count);

        i.set(5);
        assertEquals(5, ifLoc.getAsInt());
        assertEqualsLazy(5, lazyIfLoc);
        assertEquals(4, cl.count);
    }

    public void testIndirectSeqIf() {
        // @@@ Lazy binding tests don't yet work
        BooleanLocation b = BooleanVariable.make(true);
        final SequenceVariable<Integer> x = SequenceVariable.make(TypeInfo.Integer, Sequences.make(TypeInfo.Integer, 1, 2, 3));
        final SequenceVariable<Integer> y = SequenceVariable.make(TypeInfo.Integer, Sequences.make(TypeInfo.Integer, 4, 5, 6));
        SequenceLocation<Integer> ifLoc = BoundOperators.makeBoundIf(TypeInfo.Integer, false, b,
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
        SequenceLocation<Integer> lazyIfLoc = BoundOperators.makeBoundIf(TypeInfo.Integer, true, b,
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
        ifLoc.addSequenceChangeListener(hl);
        assertEqualsAndClear(hl);
        assertEquals(ifLoc.getAsSequence(), 1, 2, 3);
        assertEqualsLazy(lazyIfLoc, 1, 2, 3);

        x.insert(4);
        assertEquals(ifLoc.getAsSequence(), 1, 2, 3, 4);
        assertEqualsLazy(lazyIfLoc, 1, 2, 3, 4);
        assertEqualsAndClear(hl, "[3, 2] => [ 4 ]");

        b.set(false);
        assertEquals(ifLoc.getAsSequence(), 4, 5, 6);
        assertEqualsLazy(lazyIfLoc, 4, 5, 6);
        assertEqualsAndClear(hl, "[0, 3] => [ 4, 5, 6 ]");

        x.insert(5);
        assertEquals(ifLoc.getAsSequence(), 4, 5, 6);
        assertEquals(lazyIfLoc.getAsSequence(), 4, 5, 6);
        assertEqualsAndClear(hl);

        y.insert(7);
        assertEquals(ifLoc.getAsSequence(), 4, 5, 6, 7);
        assertEqualsLazy(lazyIfLoc, 4, 5, 6, 7);
        assertEqualsAndClear(hl, "[3, 2] => [ 7 ]");

        b.set(true);
        assertEquals(ifLoc.getAsSequence(), 1, 2, 3, 4, 5);
        assertEqualsLazy(lazyIfLoc, 1, 2, 3, 4, 5);
        assertEqualsAndClear(hl, "[0, 3] => [ 1, 2, 3, 4, 5 ]");
    }
}
