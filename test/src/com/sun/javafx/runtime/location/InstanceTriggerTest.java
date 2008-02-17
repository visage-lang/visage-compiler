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
import com.sun.javafx.runtime.sequence.Sequences;
import com.sun.javafx.runtime.sequence.Sequence;

/**
 * InstanceTriggerTest
 *
 * @author Brian Goetz
 */
public class InstanceTriggerTest extends JavaFXTestCase {

    public void testChangeTrigger() {
        final IntLocation v = IntVariable.make(3);
        CountingListener cl = new CountingListener();
        v.addChangeListener(cl);
        assertEquals(0, cl.count);
        v.setAsInt(4);
        assertEquals(1, cl.count);
        v.setAsInt(4);
        assertEquals(1, cl.count);
    }

    public void testSequenceChangeTrigger() {
        final SequenceLocation<Integer> v = SequenceVariable.make(Sequences.make(Integer.class, 1, 2, 3));
        Sequence<Integer> otherSeq = Sequences.make(Integer.class, 1, 2, 3, 5);
        Sequence<Integer> otherButEqualSeq = Sequences.make(Integer.class, 1, 2, 3, 5);
        CountingListener cl = new CountingListener();
        v.addChangeListener(cl);
        assertEquals(0, cl.count);
        v.setAsSequence(otherSeq);
        assertEquals(1, cl.count);
        v.setAsSequence(otherButEqualSeq);
        assertEquals(1, cl.count);
    }

    /** This test relies on System.gc() actually performing a collection to test that dead listeners are actually
     * (lazily) removed from the listener list.  
     */
    public void testWeakRef() {
        final IntLocation v = IntVariable.make(3);
        for (int i=0; i<1; i++) {
            CountingListener cl = new CountingListener();
            v.addWeakListener(cl);
            assertEquals(0, cl.count);
            v.setAsInt(v.getAsInt() + 1);
            assertEquals(1, cl.count);
            assertEquals(i+1, ((AbstractLocation) v).getListenerCount());
        }

        // "Force" GC, make sure weak listener goes away
        System.gc();
        v.setAsInt(0);
        assertEquals(0, ((AbstractLocation) v).getListenerCount());

        // "Force" GC, make sure weak listener stays around
        CountingListener cl = new CountingListener();
        v.addWeakListener(cl);
        assertEquals(0, cl.count);
        v.setAsInt(2);
        assertEquals(1, cl.count);
        assertEquals(1, ((AbstractLocation) v).getListenerCount());
        System.gc();
        v.setAsInt(5);
        assertEquals(1, ((AbstractLocation) v).getListenerCount());

        // "Force" GC, make sure strong but out-of-scope listener stays around
        final IntLocation u = IntVariable.make(3);
        for (int i=0; i<1; i++) {
            cl = new CountingListener();
            v.addChangeListener(cl);
            assertEquals(0, cl.count);
            v.setAsInt(v.getAsInt() + 1);
            assertEquals(1, cl.count);
        }
        System.gc();
        v.setAsInt(0);
        assertEquals(1, ((AbstractLocation) v).getListenerCount());
    }

    public void testPrevValue() {
        final IntLocation v = IntVariable.make();
        final int[] last = new int[1];
        v.addChangeListener(new IntChangeListener() {
            public void onChange(int oldValue, int newValue) {
                last[0] = oldValue;
            }
        });

        v.setAsInt(3);
        assertEquals(0, last[0]);
        v.setAsInt(4);
        assertEquals(3, last[0]);
        v.setAsInt(5);
        assertEquals(4, last[0]);
    }
}
