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

    /**
     * bind c = a + b
     */
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

    /**
     * bind c = a + b
     */
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

    public void testWeakRef() {
        final IntLocation v = IntVar.make(3);
        IntLocation vPlusOne = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return v.get() + 1;
            }
        }, v);
        assertEqualsLazy(4, vPlusOne);
        v.set(5);
        assertEquals(6, vPlusOne);
        assertEquals(1, ((AbstractLocation) v).getListenerCount());

        // "Force" GC, make sure listener stays around
        System.gc();
        v.set(5);
        assertEquals(1, ((AbstractLocation) v).getListenerCount());

        // "Force" GC, make sure listener goes away
        vPlusOne = null;
        System.gc();
        v.set(0);
        assertEquals(0, ((AbstractLocation) v).getListenerCount());
    }

    public void testIncrementalUpdates() {
        final IntLocation a = IntVar.make(0);
        final IntLocation b = IntVar.make(0);
        final IntLocation c = IntVar.make(0);
        final IntLocation d = IntVar.make(0);
        final IntLocation x = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return a.get() + b.get();
            }
        }, a, b);
        final IntLocation y = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return c.get() + d.get();
            }
        }, c, d);
        final IntLocation z = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return x.get() + y.get();
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

        a.set(1);
        assertEquals(x.get(), 1);
        assertEquals(y.get(), 0);
        assertEquals(z.get(), 1);
        assertEquals(1, aCounter.count);
        assertEquals(0, bCounter.count);
        assertEquals(0, cCounter.count);
        assertEquals(0, dCounter.count);
        assertEquals(1, xCounter.count);
        assertEquals(0, yCounter.count);
        assertEquals(1, zCounter.count);

        a.set(1);
        assertEquals(x.get(), 1);
        assertEquals(y.get(), 0);
        assertEquals(z.get(), 1);
        assertEquals(1, aCounter.count);
        assertEquals(0, bCounter.count);
        assertEquals(0, cCounter.count);
        assertEquals(0, dCounter.count);
        assertEquals(1, xCounter.count);
        assertEquals(0, yCounter.count);
        assertEquals(1, zCounter.count);

        b.set(1);
        assertEquals(x.get(), 2);
        assertEquals(y.get(), 0);
        assertEquals(z.get(), 2);
        assertEquals(1, aCounter.count);
        assertEquals(1, bCounter.count);
        assertEquals(0, cCounter.count);
        assertEquals(0, dCounter.count);
        assertEquals(2, xCounter.count);
        assertEquals(0, yCounter.count);
        assertEquals(2, zCounter.count);
    }
}
