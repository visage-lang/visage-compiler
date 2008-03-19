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

package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.*;

/**
 * BoundSequences
 *
 * @author Brian Goetz
 */
public class BoundSequences {
    /**
     * Construct a bound sequence of the form
     *   bind [ a, b, ... ]
     * where a, b, ..., are sequence locations.
     *  
     */
    public static <T> SequenceLocation<T> concatenate(Class<T> clazz, SequenceLocation<? extends T>... locations) {
        return new BoundCompositeSequence<T>(clazz, locations);
    }

    /**
     * Construct a bound sequence of the form
     *   bind [ a, b, ... ]
     * where a, b, ..., are sequence locations.
     *
     */
    public static <T> SequenceLocation<T> concatenate(Class<T> clazz, SequenceLocation<? extends T>[] locations, int size) {
        return new BoundCompositeSequence<T>(clazz, locations, size);
    }

    /**
     * Construct a bound sequence of the form
     *   bind a
     * where a is a sequence location of a subtype of the desired element type
     *
     */
    @SuppressWarnings("unchecked")
    public static <T, V extends T> SequenceLocation<T> upcast(Class<T> clazz, SequenceLocation<V> location) {
        Class<V> vClass = location.getAsSequence().getElementType();
        if (clazz == vClass)
            return (SequenceLocation<T>) location;
        else if (!clazz.isAssignableFrom(vClass))
            throw new ClassCastException("Cannot upcast Sequence<" + vClass.getName()
                    + "> to Sequence<" + clazz.getName() + ">");
        else
            return new BoundUpcastSequence<T, V>(clazz, location);
    }

    /** Construct a bound sequence of the form
     *   bind reverse x
     * where x is a sequence.
     */
    public static<T> SequenceLocation<T> reverse(SequenceLocation<T> sequence) {
        return new BoundReverseSequence<T>(sequence);
    }

    /** Construct a bound sequence of the form
     *   bind [ x ]
     * where x is an instance.
     */
    public static<T, V extends T> SequenceLocation<T> singleton(Class<T> clazz, ObjectLocation<V> location) {
        return new BoundSingletonSequence<T, V>(clazz, location);
    }

    /** Construct a bound sequence of the form
     *   bind [ x ]
     * where x is an Integer instance.
     */
    public static<T> SequenceLocation<Integer> singleton(IntLocation location) {
        return new BoundSingletonSequence<Integer, Integer>(Integer.class, location);
    }

    public static<T> SequenceLocation<T> empty(final Class<T> clazz) {
        return new AbstractBoundSequence<T>(clazz) {
            protected Sequence<T> computeValue() {
                return Sequences.emptySequence(clazz);
            }

            protected void initialize() { }
        };
    }

    public static<T> ObjectLocation<T> element(SequenceLocation<T> sequence, IntLocation index) {
        return new BoundSequenceElement<T>(sequence, index);
    }

    public static<T> IntLocation sizeof(final SequenceLocation<T> sequence) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return Sequences.size(sequence.get());
            }
        }, sequence);
    }

    public static<T> IntLocation sizeof(final ObjectLocation<T> item) {
        return IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return item.get() == null ? 0 : 1;
            }
        }, item);
    }

    public static SequenceLocation<Integer> range(IntLocation a, IntLocation b) {
        return new BoundIntRangeSequence(a, b);
    }
    
    public static SequenceLocation<Integer> range(IntLocation a, IntLocation b, IntLocation step) {
        return new BoundIntRangeSequence(a, b, step);
    }

    public static SequenceLocation<Integer> range(IntLocation a, IntLocation b, boolean exclusive) {
        return new BoundIntRangeSequence(a, b, exclusive);
    }

    public static SequenceLocation<Integer> range(IntLocation a, IntLocation b, IntLocation step, boolean exclusive) {
        return new BoundIntRangeSequence(a, b, step, exclusive);
    }

    
    public static SequenceLocation<Double> range(DoubleLocation a, DoubleLocation b) {
        return new BoundNumberRangeSequence(a, b);
    }
    
    public static SequenceLocation<Double> range(DoubleLocation a, DoubleLocation b, DoubleLocation step) {
        return new BoundNumberRangeSequence(a, b, step);
    }

    public static SequenceLocation<Double> range(DoubleLocation a, DoubleLocation b, boolean exclusive) {
        return new BoundNumberRangeSequence(a, b, exclusive);
    }

    public static SequenceLocation<Double> range(DoubleLocation a, DoubleLocation b, DoubleLocation step, boolean exclusive) {
        return new BoundNumberRangeSequence(a, b, step, exclusive);
    }
    
    public static<T> SequenceLocation<T> slice(Class<T> clazz, SequenceLocation<T> sequence, IntLocation a, IntLocation b) {
        return new BoundSequenceSlice<T>(clazz, sequence, a, b);
    }
}
