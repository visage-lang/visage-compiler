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

    // Need similar tests for Double, Boolean wrappers

}
