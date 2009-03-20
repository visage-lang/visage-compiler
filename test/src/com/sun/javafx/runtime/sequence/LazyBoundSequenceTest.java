package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.JavaFXTestCase;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceVariable;
import com.sun.javafx.runtime.location.HistoryReplaceListener;

/**
 * LazyBoundSequenceTest
 *
 * @author Brian Goetz
 */
public class LazyBoundSequenceTest extends JavaFXTestCase {
    public void testReverse() {
        SequenceLocation<Integer> i = SequenceVariable.make(TypeInfo.Integer, Sequences.range(1, 3));
        SequenceLocation<Integer> ri = BoundSequences.reverse(true, i);
        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        ri.addChangeListener(hl);
        assertEqualsAndClear(hl);
        assertEqualsLazy(ri, 3, 2, 1);
        assertEqualsAndClear(hl, "[0, -1] => [ 3, 2, 1 ]");
        i.insert(4);
        assertEqualsAndClear(hl);
        assertEqualsLazy(ri, 4, 3, 2, 1);
        assertEqualsAndClear(hl, "[0, 2] => [ 4, 3, 2, 1 ]");
        i.insert(5);
        assertEqualsAndClear(hl);
        i.insert(6);
        assertEqualsAndClear(hl);
        assertEqualsLazy(ri, 6, 5, 4, 3, 2, 1);
        assertEqualsAndClear(hl, "[0, 3] => [ 6, 5, 4, 3, 2, 1 ]");
    }
}
