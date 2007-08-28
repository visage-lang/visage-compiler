package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.JavaFXTestCase;


/**
 * LocationBindingTest
 *
 * @author Brian Goetz
 */
public class IntExpressionBindingTest extends JavaFXTestCase {

    /**
     * Test IntLocation with no binding
     */
    public void testConstantLocation() {
        final IntLocation loc = IntVar.make(3);
        assertTrue(!loc.isLazy());
        assertEquals(3, loc);
        loc.set(5);
        assertEquals(5, loc);
        assertThrows(UnsupportedOperationException.class,
                new VoidCallable() {
                    public void call() throws Exception {
                        loc.invalidate();
                    }
                });
    }

    /**
     * Test that expression locations are initially invalid
     */
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
     * a = n
     * b = bind a + 1
     * c = bind b
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

    /**
     * bind lazy b = a + 1
     */
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

    /** bind c = a + b */
    public void testDouble() {
        final DoubleLocation a = DoubleVar.make(0);
        final DoubleLocation b = DoubleVar.make(0);
        final DoubleLocation c = DoubleExpression.make(new DoubleBindingExpression() {
            public double get() {
                return a.get() + b.get();
            }
        }, a, b);
        assertEqualsLazy(0.0, c);
        a.set(1.2);
        assertEquals(1.2, c);
        b.set(4.2);
        assertEquals(5.4, c);
    }

    /** bind c = a + b */
    public void testString() {
        final ObjectLocation<String> a = ObjectVar.make("foo");
        final ObjectLocation<String> b = ObjectVar.make(" bar");
        final ObjectLocation<String> c = ObjectExpression.make(new ObjectBindingExpression<String>() {
            public String get() {
                return a.get() + b.get();
            }
        }, a, b);
        assertEqualsLazy("foo bar", c);
        a.set("yoo ");
        assertEquals("yoo  bar", c);
        b.set("hoo");
        assertEquals("yoo hoo", c);
    }
}
