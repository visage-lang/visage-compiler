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
package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.Sequences;
import junit.framework.TestCase;
import junit.framework.Assert;

import java.util.Collection;
import java.util.HashSet;

/**
 * JavaFXTestCase
 *
 * @author Brian Goetz
 */
public abstract class JavaFXTestCase extends TestCase {
    private static final double EPSILON = 0.00000001;

    /**
     * Helper method for asserting that a sequence contains a specific set of values; tests via Object.equals(),
     * equality of toString(), by iterating the elements, and by toArray
     */
    protected <T> void assertEquals(Sequence<T> sequence, T... values) {
        Sequence<T> newSeq = Sequences.make(sequence.getElementType(), values);
        assertEquals(sequence, newSeq);

        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        for (int i = 0; i < values.length; i++) {
            if (i != 0)
                sb.append(", ");
            sb.append(values[i]);
        }
        sb.append(" ]");
        assertEquals(sb.toString(), sequence.toString());

        int index = 0;
        for (T t : sequence) {
            assertEquals(t, values[index++]);
        }

        T[] array = (T[]) new Object[sequence.size()];
        sequence.toArray(array, 0);
        assertEquals(array.length, values.length);
        for (int i = 0; i < array.length; i++)
            assertEquals(array[i], values[i]);
    }

    protected void assertEquals(Sequence<Double> sequence, Double... values) {
        assertEquals(sequence.size(), values.length);
        int index = 0;
        for (Double t : sequence) {
            Double value = values[index++];
            assertTrue(value + " !~ " + t, Math.abs(t - value) < EPSILON);
        }
    }

    protected <T> void assertEquals(Sequence<T> sequence, T value) {
        assertEquals(sequence, Sequences.singleton(sequence.getElementType(), value));
    }

    protected <T> void assertEquals(SequenceLocation<T> sequence, T... values) {
        assertEquals(sequence.get(), values);
    }

    protected void assertEquals(HistorySequenceListener hl, String... values) {
        assertEquals(values.length, hl.get().size());
        for (int i=0; i<values.length; i++)
            Assert.assertEquals(values[i], hl.get().get(i));
    }

    protected void assertEquals(HistorySequenceListener hl, String value) {
        assertEquals(hl, new String[] { value });
    }

    protected void assertEquals(int value, IntLocation loc) {
        assertTrue(loc.isValid());
        assertEquals(value, loc.get());
    }

    protected void assertEqualsLazy(int value, IntLocation loc) {
        assertFalse(loc.isValid());
        assertEquals(value, loc.get());
        assertTrue(loc.isValid());
    }

    protected void assertEquals(double value, DoubleLocation loc) {
        assertTrue(loc.isValid());
        assertEquals(value, loc.get());
    }

    protected void assertEqualsLazy(double value, DoubleLocation loc) {
        assertFalse(loc.isValid());
        assertEquals(value, loc.get());
        assertTrue(loc.isValid());
    }

    protected<T> void assertEquals(T value, ObjectLocation<T> loc) {
        assertTrue(loc.isValid());
        assertEquals(value, loc.get());
    }

    protected<T> void assertEqualsLazy(T value, ObjectLocation<T> loc) {
        assertFalse(loc.isValid());
        assertEquals(value, loc.get());
        assertTrue(loc.isValid());
    }

    protected<T> void assertEquals(Collection<T> collection, T... values) {
        Collection<T> newCollection = new HashSet<T>();
        for (T val : values)
            newCollection.add(val);
        assertEquals(collection, newCollection);
    }

    protected interface VoidCallable {
        public void call() throws Exception;
    }

    protected static void assertThrows(Class<? extends Exception> exceptionClass, VoidCallable closure) {
        try {
            closure.call();
            fail("Expected exception " + exceptionClass.getCanonicalName());
        }
        catch (Exception e) {
            if (exceptionClass.isInstance(e))
                return;
            else
                throw new RuntimeException("Expecting exception " + exceptionClass.getCanonicalName() + "; found exception " + e.getClass().getCanonicalName(), e);
        }
    }

}
