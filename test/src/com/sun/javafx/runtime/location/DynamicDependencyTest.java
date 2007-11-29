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
 * DynamicDependencyTest
 *
 * @author Brian Goetz
 */
public class DynamicDependencyTest extends JavaFXTestCase {

    public void testStaticDependencies() {
        final IntLocation a = IntVar.make(1);
        final IntLocation b = IntVar.make(1);
        final IntLocation c = IntExpression.make(new IntBindingExpression() {
            public int get() {
                return a.get() + b.get();
            }
        }, a, b);

        assertEquals(2, c.get());
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());

        a.set(3);
        b.set(3);
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());
    }

    
    /** This test relies on System.gc() actually performing a collection to test that dead listeners are actually
     * (lazily) removed from the listener list.
     */
    public void testDynamicOnly() {
        final IntLocation a = IntVar.make(1);
        final IntLocation b = IntVar.make(1);
        final IntLocation c = IntExpression.make(new IntBindingExpression() {
            public int get() {
                clearDynamicDependencies();
                addDynamicDependent(a);
                addDynamicDependent(b);
                return a.get() + b.get();
            }
        });

        assertEquals(0, ((AbstractLocation) a).getListenerCount());
        assertEquals(0, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());

        assertEquals(2, c.get());
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());

        System.gc();
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());

        a.set(3);
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());

        assertEquals(4, c.get());
        b.set(3);
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());

        assertEquals(6, c.get());
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());
    }

    /** This test relies on System.gc() actually performing a collection to test that dead listeners are actually
     * (lazily) removed from the listener list.
     */
    public void testStaticAndDynamic() {
        final IntLocation a = IntVar.make(1);
        final IntLocation b = IntVar.make(1);
        final IntLocation c = IntExpression.make(new IntBindingExpression() {
            public int get() {
                clearDynamicDependencies();
                addDynamicDependent(b);
                return a.get() + b.get();
            }
        }, a);

        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(0, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());

        assertEquals(2, c.get());
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());

        System.gc();
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());

        a.set(3);
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());

        assertEquals(4, c.get());
        b.set(3);
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());

        assertEquals(6, c.get());
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());
    }
}
