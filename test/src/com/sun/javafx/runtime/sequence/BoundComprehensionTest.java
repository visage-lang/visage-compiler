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
        final SequenceLocation<Integer> derived = new SimpleBoundComprehension<Integer, Integer>(Integer.class, base,
                new SimpleBoundComprehension.ElementGenerator<Integer, Integer>() {
                    public Integer getValue(Integer element, int index) {
                        return element * 2;
                    }
                });
        IntLocation len = new IntExpression(false, derived) {
            protected int computeValue() {
                return Sequences.size(derived.get());
            }
        };

        HistoryReplaceListener<Integer> hl = new HistoryReplaceListener<Integer>();
        derived.addChangeListener(hl);

        assertEquals(derived, 2, 4, 6);
        assertEquals(3, len.get());

        base.insert(4);
        assertEquals(derived, 2, 4, 6, 8);
        assertEquals(4, len.get());
        assertEqualsAndClear(hl, "[3, 2] => [ 8 ]");

        base.delete(0);
        assertEquals(derived, 4, 6, 8);
        assertEquals(3, len.get());
        System.out.println(hl.get());
        assertEqualsAndClear(hl, "[0, 0] => [ ]");

        base.set(0, 0);
        assertEquals(derived, 0, 6, 8);
        assertEquals(3, len.get());
        assertEqualsAndClear(hl, "[0, 0] => [ 0 ]");
    }
}
