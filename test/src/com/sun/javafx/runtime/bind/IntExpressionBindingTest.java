package com.sun.javafx.runtime.bind;

import com.sun.javafx.runtime.location.*;
import junit.framework.TestCase;

import javax.swing.event.ChangeEvent;


/**
 * LocationBindingTest
 *
 * @author Brian Goetz
 */
public class IntExpressionBindingTest extends TestCase {

    private void assertEquals(int value, IntLocation loc) {
        assertTrue(loc.isValid());
        assertEquals(value, loc.get());
    }

    private void assertEqualsLazy(int value, IntLocation loc) {
        assertFalse(loc.isValid());
        assertEquals(value, loc.get());
        assertTrue(loc.isValid());
    }

    private static class CountingListener implements ChangeListener {
        public int count;

        public void onChange() {
            ++count;
        }
    }

    /** Test IntLocation with no binding */
    public void testConstantLocation() {
        IntLocation loc = IntVar.make(3);
        assertTrue(!loc.isLazy());
        assertEquals(3, loc);
        loc.set(5);
        assertEquals(5, loc);
    }

    /** Test that expression locations are initially invalid */
    public void testInitiallyInvalid() {
        final IntLocation a = IntVar.make(0);
        final IntLocation b = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return a.get() + 1;
            }
        }, a);

        assertTrue(a.isValid());
        assertEqualsLazy(1, b);
    }

    /**
     * Bind three variables:
     *   a = n
     *   b = bind a + 1
     *   c = bind b
     */

    public void testSimpleBind() {
        final IntLocation a = IntVar.make(0);
        final IntLocation b = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return a.get() + 1;
            }
        }, a);
        final IntLocation c = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return b.get();
            }
        }, b);

        CountingListener counter = new CountingListener();
        c.addChangeListener(counter);

        assertTrue(a.isValid());
        assertFalse(b.isValid());
        assertFalse(c.isValid());
        assertEquals(0, a.get());

        a.set(3);
        assertEquals(3, a);
        assertEquals(4, b);
        assertEquals(4, c);
        assertEquals(1, counter.count);
    }

    /** bind lazy b = a + 1 */
    public void testLazyBind() {
        final IntLocation a = IntVar.make(0);
        final IntLocation b = IntExpression.makeLazy(new IntBindingExpression() {
            public int get() {
                return a.get() + 1;
            }
        }, a);

        a.set(2);
        assertEquals(2, a);
        assertEqualsLazy(3, b);
    }

    /**
     * bind c = a + b
     * bind lazy d = c + 1
     */
    public void testLazyBehindEager() {
        final IntLocation a = IntVar.make(0);
        final IntLocation b = IntVar.make(0);
        final IntLocation c = IntExpression.makeLazy(new IntBindingExpression() {
            public int get() {
                return a.get() + b.get();
            }
        }, a, b);
        final IntLocation d = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return c.get() + 1;
            }
        }, c);

        assertTrue(c.isLazy());
        assertFalse(d.isLazy());

        a.set(3);
        b.set(4);
        assertEquals(8, d);
        assertEquals(7, c);
    }
}
