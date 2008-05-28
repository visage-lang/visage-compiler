/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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
            { setInitialValue(Sequences.emptySequence(clazz)); }
        };
    }

    public static<T> ObjectLocation<T> element(SequenceLocation<T> sequence, IntLocation index) {
        return new BoundSequenceElement<T>(sequence, index);
    }

    public static IntLocation element(SequenceLocation<Integer> sequence, IntLocation index) {
        return Locations.asIntLocation(new BoundSequenceElement<Integer>(sequence, index));
    }

    public static DoubleLocation element(SequenceLocation<Double> sequence, IntLocation index) {
        return Locations.asDoubleLocation(new BoundSequenceElement<Double>(sequence, index));
    }

    public static BooleanLocation element(SequenceLocation<Boolean> sequence, IntLocation index) {
        return Locations.asBooleanLocation(new BoundSequenceElement<Boolean>(sequence, index));
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
        return new BoundSequenceSlice<T>(clazz, sequence, a, b, false);
    }
    
    public static<T> SequenceLocation<T> sliceExclusive(Class<T> clazz, SequenceLocation<T> sequence, IntLocation a, IntLocation b) {
        return new BoundSequenceSlice<T>(clazz, sequence, a, b, true);
    }
    
    public interface ObjectSimpleBoundComprehensionCallback<T, V> {
         V computeElement$(T element, int index);
    }

    public interface IntSimpleBoundComprehensionCallback<V> {
         V computeElement$(int element, int index);
    }

    public interface DoubleSimpleBoundComprehensionCallback<V> {
         V computeElement$(double element, int index);
    }

    public interface BooleanSimpleBoundComprehensionCallback<V> {
        V computeElement$(boolean element, int index);
    }

    public static<T, V> SequenceLocation<V> makeSimpleBoundComprehension(Class<V> clazz,
                                                                         SequenceLocation<T> seq,
                                                                         boolean useIndex,
                                                                         final ObjectSimpleBoundComprehensionCallback<T, V> callback) {
        return new SimpleBoundComprehension<T, V>(clazz, seq, useIndex) {
            protected V computeElement$(T element, int index) {
                return callback.computeElement$(element, index);
            }
        };
    }

    public static<V> SequenceLocation<V> makeSimpleBoundComprehension(Class<V> clazz,
                                                                      SequenceLocation<Integer> seq,
                                                                      boolean useIndex,
                                                                      final IntSimpleBoundComprehensionCallback<V> callback) {
        return new SimpleBoundComprehension<Integer, V>(clazz, seq, useIndex) {
            protected V computeElement$(Integer element, int index) {
                return callback.computeElement$(element, index);
            }
        };
    }

    public static<V> SequenceLocation<V> makeSimpleBoundComprehension(Class<V> clazz,
                                                                      SequenceLocation<Double> seq,
                                                                      boolean useIndex,
                                                                      final DoubleSimpleBoundComprehensionCallback<V> callback) {
        return new SimpleBoundComprehension<Double, V>(clazz, seq, useIndex) {
            protected V computeElement$(Double element, int index) {
                return callback.computeElement$(element, index);
            }
        };
    }

    public static<V> SequenceLocation<V> makeSimpleBoundComprehension(Class<V> clazz,
                                                                      SequenceLocation<Boolean> seq,
                                                                      boolean useIndex,
                                                                      final BooleanSimpleBoundComprehensionCallback<V> callback) {
        return new SimpleBoundComprehension<Boolean, V>(clazz, seq, useIndex) {
            protected V computeElement$(Boolean element, int index) {
                return callback.computeElement$(element, index);
            }
        };
    }

    public interface ObjectBoundComprehensionCallback<T, V> {
         SequenceLocation<V> computeElements$(ObjectLocation<T> elementLocation, IntLocation indexLocation);
    }

    public interface IntBoundComprehensionCallback<T> {
         SequenceLocation<T> computeElements$(IntLocation elementLocation, IntLocation indexLocation);
    }

    public interface DoubleBoundComprehensionCallback<T> {
         SequenceLocation<T> computeElements$(DoubleLocation elementLocation, IntLocation indexLocation);
    }

    public interface BooleanBoundComprehensionCallback<T> {
         SequenceLocation<T> computeElements$(BooleanLocation elementLocation, IntLocation indexLocation);
    }

    public static<T, V> SequenceLocation<V> makeBoundComprehension(Class<V> clazz,
                                                                   SequenceLocation<T> sequenceLocation,
                                                                   boolean useIndex,
                                                                   final ObjectBoundComprehensionCallback<T, V> callback) {
        return new AbstractBoundComprehension<T, ObjectLocation<T>, V>(clazz, sequenceLocation, useIndex) {
            protected ObjectLocation<T> makeInductionLocation(T value) {
                return ObjectVariable.<T>make(value);
            }

            protected SequenceLocation<V> computeElements$(ObjectLocation<T> elementLocation, IntLocation indexLocation) {
                return callback.computeElements$(elementLocation, indexLocation);
            }
        };
    }

    public static<V> SequenceLocation<V> makeBoundComprehension(Class<V> clazz,
                                                                   SequenceLocation<Integer> sequenceLocation,
                                                                   boolean useIndex,
                                                                   final IntBoundComprehensionCallback<V> callback) {
        return new AbstractBoundComprehension<Integer, IntLocation, V>(clazz, sequenceLocation, useIndex) {
            protected IntLocation makeInductionLocation(Integer value) {
                return IntVariable.make(value);
            }

            protected SequenceLocation<V> computeElements$(IntLocation elementLocation, IntLocation indexLocation) {
                return callback.computeElements$(elementLocation, indexLocation);
            }
        };
    }

    public static<V> SequenceLocation<V> makeBoundComprehension(Class<V> clazz,
                                                                SequenceLocation<Double> sequenceLocation,
                                                                boolean useIndex,
                                                                final DoubleBoundComprehensionCallback<V> callback) {
        return new AbstractBoundComprehension<Double, DoubleLocation, V>(clazz, sequenceLocation, useIndex) {
            protected DoubleLocation makeInductionLocation(Double value) {
                return DoubleVariable.make(value);
            }

            protected SequenceLocation<V> computeElements$(DoubleLocation elementLocation, IntLocation indexLocation) {
                return callback.computeElements$(elementLocation, indexLocation);
            }
        };
    }

    public static<V> SequenceLocation<V> makeBoundComprehension(Class<V> clazz,
                                                                SequenceLocation<Boolean> sequenceLocation,
                                                                boolean useIndex,
                                                                final BooleanBoundComprehensionCallback<V> callback) {
        return new AbstractBoundComprehension<Boolean, BooleanLocation, V>(clazz, sequenceLocation, useIndex) {
            protected BooleanLocation makeInductionLocation(Boolean value) {
                return BooleanVariable.make(value);
            }

            protected SequenceLocation<V> computeElements$(BooleanLocation elementLocation, IntLocation indexLocation) {
                return callback.computeElements$(elementLocation, indexLocation);
            }
        };
    }
    
}
