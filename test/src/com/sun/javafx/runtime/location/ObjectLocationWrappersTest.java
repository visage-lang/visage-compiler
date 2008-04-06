/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.BindingException;
import com.sun.javafx.runtime.JavaFXTestCase;

/**
 * ObjectLocationWrappersTest
 *
 * @author Brian Goetz
 */
public class ObjectLocationWrappersTest extends JavaFXTestCase {

    public void testIntObjectWrapper() {
        final IntLocation i = IntVariable.make(0);
        final IntLocation ie = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return i.getAsInt() + 1;
            }
        }, i);
        final ObjectLocation<Integer> oi = Locations.asObjectLocation(i);
        final ObjectLocation<Integer> oie = Locations.asObjectLocation(ie);

        assertTrue(oi.isMutable());
        assertFalse(oie.isMutable());

        assertEquals(i.getAsInt(), oi.get().intValue());
        i.setAsInt(0);
        assertEquals(i.getAsInt(), oi.get().intValue());
        i.setAsInt(Integer.MAX_VALUE);
        assertEquals(i.getAsInt(), oi.get().intValue());
        i.setAsInt(Integer.MIN_VALUE);
        assertEquals(i.getAsInt(), oi.get().intValue());
        oi.set(0);
        assertEquals(i.getAsInt(), oi.get().intValue());
        oi.set(Integer.MAX_VALUE);
        assertEquals(i.getAsInt(), oi.get().intValue());
        oi.set(Integer.MIN_VALUE);
        assertEquals(i.getAsInt(), oi.get().intValue());

        i.setAsInt(0);
        assertEquals(1, ie.getAsInt());
        assertEquals(1, oie.get().intValue());

        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                ie.setAsInt(3);
            }
        });
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                oie.set(3);
            }
        });
    }

    public void testObjectIntWrapper() {
        ObjectVariable<Integer> o = ObjectVariable.make(2);
        IntLocation i = Locations.asIntLocation(o);

        assertEquals(2, i);
        o.set(3);
        assertEquals(3, i);
        i.setAsInt(4);
        assertEquals(4, o);
    }

    public void testDoubleObjectWrapper() {
        final DoubleLocation i = DoubleVariable.make(0);
        final DoubleLocation ie = DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return i.getAsDouble() + 1;
            }
        }, i);
        final ObjectLocation<Double> oi = Locations.asObjectLocation(i);
        final ObjectLocation<Double> oie = Locations.asObjectLocation(ie);

        assertTrue(oi.isMutable());
        assertFalse(oie.isMutable());

        assertEquals(i.getAsDouble(), oi.get().doubleValue());
        i.setAsDouble(0);
        assertEquals(i.getAsDouble(), oi.get().doubleValue());
        i.setAsDouble(Integer.MAX_VALUE);
        assertEquals(i.getAsDouble(), oi.get().doubleValue());
        i.setAsDouble(Integer.MIN_VALUE);
        assertEquals(i.getAsDouble(), oi.get().doubleValue());
        oi.set(0.0);
        assertEquals(i.getAsDouble(), oi.get().doubleValue());
        oi.set(Double.MAX_VALUE);
        assertEquals(i.getAsDouble(), oi.get().doubleValue());
        oi.set(Double.MIN_VALUE);
        assertEquals(i.getAsDouble(), oi.get().doubleValue());

        i.setAsDouble(0);
        assertEquals(1.0, ie.getAsDouble());
        assertEquals(1.0, oie.get().doubleValue());

        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                ie.setAsDouble(3);
            }
        });
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                oie.set(3.0);
            }
        });
    }

    public void testBooleanObjectWrapper() {
        final BooleanLocation i = BooleanVariable.make(true);
        final BooleanLocation ie = BooleanVariable.make(new BooleanBindingExpression() {
            public boolean computeValue() {
                return !i.getAsBoolean();
            }
        }, i);
        final ObjectLocation<Boolean> oi = Locations.asObjectLocation(i);
        final ObjectLocation<Boolean> oie = Locations.asObjectLocation(ie);

        assertTrue(oi.isMutable());
        assertFalse(oie.isMutable());

        assertEquals(i.getAsBoolean(), oi.get().booleanValue());
        i.setAsBoolean(false);
        assertEquals(i.getAsBoolean(), oi.get().booleanValue());
        i.setAsBoolean(true);
        assertEquals(i.getAsBoolean(), oi.get().booleanValue());
        oi.set(true);
        assertEquals(i.getAsBoolean(), oi.get().booleanValue());
        oi.set(false);
        assertEquals(i.getAsBoolean(), oi.get().booleanValue());

        i.setAsBoolean(false);
        assertEquals(true, ie.getAsBoolean());
        assertEquals(true, oie.get().booleanValue());

        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                ie.setAsBoolean(false);
            }
        });
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                oie.set(!ie.getAsBoolean());
            }
        });
    }
}
