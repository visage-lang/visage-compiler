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
 * LocationBindingTest
 *
 * @author Brian Goetz
 */
public class IntExpressionBindingTest extends JavaFXTestCase {

    /**
     * Test IntLocation with no binding
     */
    public void testConstantLocation() {
        final IntVariable loc = IntVariable.make(3);
        assertTrue(!loc.isLazy());
        assertEquals(3, loc);
        loc.setAsInt(5);
        assertEquals(5, loc);
        assertException(BindingException.class, loc, "invalidate");
    }


    /**
     * Test that expression locations are initially invalid
     */
    public void testInitiallyInvalid() {
        final IntLocation a = IntVariable.make(0);
        final IntLocation b = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.getAsInt() + 1;
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
        final IntLocation a = IntVariable.make(0);
        final IntLocation b = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.getAsInt() + 1;
            }
        }, a);
        final IntLocation c = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return b.getAsInt();
            }
        }, b);

        CountingListener counter = new CountingListener();
        c.addChangeListener(counter);

        assertTrue(a.isValid());
        assertFalse(b.isValid());
        assertFalse(c.isValid());
        assertEquals(0, a.getAsInt());

        a.setAsInt(3);
        assertEquals(3, a);
        assertEquals(4, b);
        assertEquals(4, c);
        assertEquals(1, counter.count);
    }

    /**
     * bind lazy b = a + 1
     */
    public void testLazyBind() {
        final IntLocation a = IntVariable.make(0);
        final IntLocation b = IntVariable.make(true, new IntBindingExpression() {
            public int computeValue() {
                return a.getAsInt() + 1;
            }
        }, a);

        a.setAsInt(2);
        assertEquals(2, a);
        assertEqualsLazy(3, b);
    }

    /**
     * bind c = a + b
     * bind lazy d = c + 1
     */
    public void testLazyBehindEager() {
        final IntVariable a = IntVariable.make(0);
        final IntVariable b = IntVariable.make(0);
        final IntVariable c = IntVariable.make(true, new IntBindingExpression() {
            public int computeValue() {
                return a.getAsInt() + b.getAsInt();
            }
        }, a, b);
        final IntVariable d = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return c.getAsInt() + 1;
            }
        }, c);

        assertTrue(c.isLazy());
        assertFalse(d.isLazy());

        a.setAsInt(3);
        b.setAsInt(4);
        assertEquals(8, d);
        assertEquals(7, c);
    }

    /**
     * bind c = a + b
     */
    public void testDouble() {
        final DoubleLocation a = DoubleVariable.make(0);
        final DoubleLocation b = DoubleVariable.make(0);
        final DoubleLocation c = DoubleVariable.make(new DoubleBindingExpression() {
            public double computeValue() {
                return a.getAsDouble() + b.getAsDouble();
            }
        }, a, b);
        assertEqualsLazy(0.0, c);
        a.setAsDouble(1.2);
        assertEquals(1.2, c);
        b.setAsDouble(4.2);
        assertEquals(5.4, c);
    }

    /**
     * bind c = a + b
     */
    public void testString() {
        final ObjectLocation<String> a = ObjectVariable.make("foo");
        final ObjectLocation<String> b = ObjectVariable.make(" bar");
        final ObjectLocation<String> c = ObjectVariable.make(new ObjectBindingExpression<String>() {
            public String computeValue() {
                return a.get() + b.get();
            }
        }, a, b);
        assertEqualsLazy("foo bar", c);
        a.set("yoo ");
        assertEquals("yoo  bar", c);
        b.set("hoo");
        assertEquals("yoo hoo", c);
    }

    public void testWeakRef() {
        final IntLocation v = IntVariable.make(3);
        IntLocation vPlusOne = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return v.getAsInt() + 1;
            }
        }, v);
        assertEqualsLazy(4, vPlusOne);
        v.setAsInt(5);
        assertEquals(6, vPlusOne);
        assertEquals(1, ((AbstractLocation) v).getListenerCount());

        // "Force" GC, make sure listener stays around
        System.gc();
        v.setAsInt(5);
        assertEquals(1, ((AbstractLocation) v).getListenerCount());

        // "Force" GC, make sure listener goes away
        vPlusOne = null;
        System.gc();
        v.setAsInt(0);
        assertEquals(0, ((AbstractLocation) v).getListenerCount());
    }

    public void testIncrementalUpdates() {
        final IntLocation a = IntVariable.make(0);
        final IntLocation b = IntVariable.make(0);
        final IntLocation c = IntVariable.make(0);
        final IntLocation d = IntVariable.make(0);
        final IntLocation x = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.getAsInt() + b.getAsInt();
            }
        }, a, b);
        final IntLocation y = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return c.getAsInt() + c.getAsInt();
            }
        }, c, d);
        final IntLocation z = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return x.getAsInt() + y.getAsInt();
            }
        }, x, y);
        CountingListener aCounter = new CountingListener();
        CountingListener bCounter = new CountingListener();
        CountingListener cCounter = new CountingListener();
        CountingListener dCounter = new CountingListener();
        CountingListener xCounter = new CountingListener();
        CountingListener yCounter = new CountingListener();
        CountingListener zCounter = new CountingListener();
        a.addChangeListener(aCounter);
        b.addChangeListener(bCounter);
        c.addChangeListener(cCounter);
        d.addChangeListener(dCounter);
        x.addChangeListener(xCounter);
        y.addChangeListener(yCounter);
        z.addChangeListener(zCounter);

        a.setAsInt(1);
        assertEquals(x.getAsInt(), 1);
        assertEquals(y.getAsInt(), 0);
        assertEquals(z.getAsInt(), 1);
        assertEquals(1, aCounter.count);
        assertEquals(0, bCounter.count);
        assertEquals(0, cCounter.count);
        assertEquals(0, dCounter.count);
        assertEquals(1, xCounter.count);
        assertEquals(1, yCounter.count);
        assertEquals(1, zCounter.count);

        a.setAsInt(1);
        assertEquals(x.getAsInt(), 1);
        assertEquals(y.getAsInt(), 0);
        assertEquals(z.getAsInt(), 1);
        assertEquals(1, aCounter.count);
        assertEquals(0, bCounter.count);
        assertEquals(0, cCounter.count);
        assertEquals(0, dCounter.count);
        assertEquals(1, xCounter.count);
        assertEquals(1, yCounter.count);
        assertEquals(1, zCounter.count);

        b.setAsInt(1);
        assertEquals(x.getAsInt(), 2);
        assertEquals(y.getAsInt(), 0);
        assertEquals(z.getAsInt(), 2);
        assertEquals(1, aCounter.count);
        assertEquals(1, bCounter.count);
        assertEquals(0, cCounter.count);
        assertEquals(0, dCounter.count);
        assertEquals(2, xCounter.count);
        assertEquals(1, yCounter.count);
        assertEquals(2, zCounter.count);
    }
}
