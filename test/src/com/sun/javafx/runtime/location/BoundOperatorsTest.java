package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.JavaFXTestCase;

/**
 * BoundOperatorsTest
 *
 * @author Brian Goetz
 */
public class BoundOperatorsTest extends JavaFXTestCase {
    public void testIf() {
        BooleanLocation b = BooleanVariable.make(true);
        final IntVariable i = IntVariable.make(1);
        IntLocation ifLoc = new BoundIntIfExpression(b, false) {
            protected IntLocation computeThenBranch() {
                return IntVariable.make(new IntBindingExpression() {
                    public int computeValue() {
                        return i.get();
                    }
                }, i);
            }

            protected IntLocation computeElseBranch() {
                return IntVariable.make(new IntBindingExpression() {
                    public int computeValue() {
                        return -i.get();
                    }
                }, i);
            }
        };
        CountingListener cl = new CountingListener();
        ifLoc.addChangeListener(cl);

        assertEquals(1, ifLoc.getAsInt());
        assertEquals(1, cl.count);

        b.set(true);
        assertEquals(1, ifLoc.getAsInt());
        assertEquals(1, cl.count);

        b.set(false);
        assertEquals(-1, ifLoc.getAsInt());
        assertEquals(2, cl.count);

        i.set(3);
        assertEquals(-3, ifLoc.getAsInt());
        assertEquals(3, cl.count);

        i.set(3);
        assertEquals(-3, ifLoc.getAsInt());
        assertEquals(3, cl.count);

        b.set(true);
        assertEquals(3, ifLoc.getAsInt());
        assertEquals(4, cl.count);

        i.set(3);
        assertEquals(3, ifLoc.getAsInt());
        assertEquals(4, cl.count);

        i.set(5);
        assertEquals(5, ifLoc.getAsInt());
        assertEquals(5, cl.count);
    }
}
