package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.JavaFXTestCase;

/**
 * ObjectLocationWrappersTest
 *
 * @author Brian Goetz
 */
public class ObjectLocationWrappersTest extends JavaFXTestCase {

    public void testIntObjectWrapper() {
        final IntLocation i = IntVar.make(0);
        final IntLocation ie = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return i.get() + 1;
            }
        });
        final ObjectLocation<Integer> oi = i.asIntegerLocation();
        final ObjectLocation<Integer> oie = ie.asIntegerLocation();

        assertTrue(oi instanceof MutableLocation);
        assertFalse(oie instanceof MutableLocation);

        assertEquals(i.get(), oi.get().intValue());
        i.set(0);
        assertEquals(i.get(), oi.get().intValue());
        i.set(Integer.MAX_VALUE);
        assertEquals(i.get(), oi.get().intValue());
        i.set(Integer.MIN_VALUE);
        assertEquals(i.get(), oi.get().intValue());
        oi.set(0);
        assertEquals(i.get(), oi.get().intValue());
        oi.set(Integer.MAX_VALUE);
        assertEquals(i.get(), oi.get().intValue());
        oi.set(Integer.MIN_VALUE);
        assertEquals(i.get(), oi.get().intValue());

        i.set(0);
        assertEquals(1, ie.get());
        assertEquals(1, oie.get().intValue());

        assertThrows(UnsupportedOperationException.class, new VoidCallable() {
            public void call() throws Exception {
                ie.set(3);
            }
        });
        assertThrows(UnsupportedOperationException.class, new VoidCallable() {
            public void call() throws Exception {
                oie.set(3);
            }
        });
    }

    public void testDoubleObjectWrapper() {
        final DoubleLocation i = DoubleVar.make(0);
        final DoubleLocation ie = DoubleExpression.make(new DoubleBindingExpression() {
            public double get() {
                return i.get() + 1;
            }
        });
        final ObjectLocation<Double> oi = i.asDoubleLocation();
        final ObjectLocation<Double> oie = ie.asDoubleLocation();

        assertTrue(oi instanceof MutableLocation);
        assertFalse(oie instanceof MutableLocation);

        assertEquals(i.get(), oi.get().doubleValue());
        i.set(0);
        assertEquals(i.get(), oi.get().doubleValue());
        i.set(Integer.MAX_VALUE);
        assertEquals(i.get(), oi.get().doubleValue());
        i.set(Integer.MIN_VALUE);
        assertEquals(i.get(), oi.get().doubleValue());
        oi.set(0.0);
        assertEquals(i.get(), oi.get().doubleValue());
        oi.set(Double.MAX_VALUE);
        assertEquals(i.get(), oi.get().doubleValue());
        oi.set(Double.MIN_VALUE);
        assertEquals(i.get(), oi.get().doubleValue());

        i.set(0);
        assertEquals(1.0, ie.get());
        assertEquals(1.0, oie.get().doubleValue());

        assertThrows(UnsupportedOperationException.class, new VoidCallable() {
            public void call() throws Exception {
                ie.set(3);
            }
        });
        assertThrows(UnsupportedOperationException.class, new VoidCallable() {
            public void call() throws Exception {
                oie.set(3.0);
            }
        });
    }

    public void testBooleanObjectWrapper() {
        final BooleanLocation i = BooleanVar.make(true);
        final BooleanLocation ie = BooleanExpression.make(new BooleanBindingExpression() {
            public boolean get() {
                return !i.get();
            }
        });
        final ObjectLocation<Boolean> oi = i.asBooleanLocation();
        final ObjectLocation<Boolean> oie = ie.asBooleanLocation();

        assertTrue(oi instanceof MutableLocation);
        assertFalse(oie instanceof MutableLocation);

        assertEquals(i.get(), oi.get().booleanValue());
        i.set(false);
        assertEquals(i.get(), oi.get().booleanValue());
        i.set(true);
        assertEquals(i.get(), oi.get().booleanValue());
        oi.set(true);
        assertEquals(i.get(), oi.get().booleanValue());
        oi.set(false);
        assertEquals(i.get(), oi.get().booleanValue());

        i.set(false);
        assertEquals(true, ie.get());
        assertEquals(true, oie.get().booleanValue());

        assertThrows(UnsupportedOperationException.class, new VoidCallable() {
            public void call() throws Exception {
                ie.set(false);
            }
        });
        assertThrows(UnsupportedOperationException.class, new VoidCallable() {
            public void call() throws Exception {
                oie.set(true);
            }
        });
    }
}
