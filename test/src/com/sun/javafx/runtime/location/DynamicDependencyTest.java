/*
 * Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
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
        final IntLocation a = IntVariable.make(1);
        final IntLocation b = IntVariable.make(1);
        final IntLocation c = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return a.getAsInt() + b.getAsInt();
            }
        }, a, b);

        assertEquals(2, c.getAsInt());
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());

        a.setAsInt(3);
        b.setAsInt(3);
        assertEquals(1, ((AbstractLocation) a).getListenerCount());
        assertEquals(1, ((AbstractLocation) b).getListenerCount());
        assertEquals(0, ((AbstractLocation) c).getListenerCount());
    }
}
