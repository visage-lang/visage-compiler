/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.runtime.NumericTypeInfo;
import com.sun.javafx.runtime.TypeInfo;
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
    public static <T> SequenceLocation<T> concatenate(TypeInfo<T> typeInfo, SequenceLocation<? extends T>... locations) {
        return new BoundCompositeSequence<T>(typeInfo, locations);
    }

    /**
     * Construct a bound sequence of the form
     *   bind [ a, b, ... ]
     * where a, b, ..., are sequence locations.
     *
     */
    public static <T> SequenceLocation<T> concatenate(TypeInfo<T> typeInfo, SequenceLocation<? extends T>[] locations, int size) {
        return new BoundCompositeSequence<T>(typeInfo, locations, size);
    }

    /**
     * Construct a bound sequence of the form
     *   bind a
     * where a is a sequence location of a subtype of the desired element type
     *
     */
    public static <T, V extends T> SequenceLocation<T> upcast(TypeInfo<T> typeInfo, SequenceLocation<V> location) {
        return new BoundUpcastSequence<T, V>(typeInfo, location);
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
    public static<T, V extends T> SequenceLocation<T> singleton(TypeInfo<T> typeInfo, ObjectLocation<V> location) {
        return new BoundSingletonSequence<T, V>(typeInfo, location);
    }

    /** Construct a bound sequence of the form
     *   bind [ x ]
     * where x is an Integer instance.
     */
    public static<T> SequenceLocation<Integer> singleton(IntLocation location) {
        return new BoundSingletonSequence<Integer, Integer>(TypeInfo.Integer, location);
    }

    public static<T> SequenceLocation<T> empty(final TypeInfo<T> typeInfo) {
        return new AbstractBoundSequence<T>(typeInfo) {
            { setInitialValue(typeInfo.emptySequence); }
        };
    }

    public static<T> ObjectLocation<T> element(SequenceLocation<T> sequence, IntLocation index) {
        return new BoundSequenceElement<T>(sequence, index);
    }

    public static IntLocation element(SequenceLocation<Integer> sequence, IntLocation index) {
        return Locations.asIntLocation(new BoundSequenceElement<Integer>(sequence, index));
    }

    public static FloatLocation element(SequenceLocation<Float> sequence, IntLocation index) {
        return Locations.asFloatLocation(new BoundSequenceElement<Float>(sequence, index));
    }

    public static BooleanLocation element(SequenceLocation<Boolean> sequence, IntLocation index) {
        return Locations.asBooleanLocation(new BoundSequenceElement<Boolean>(sequence, index));
    }

    public static<T> IntLocation sizeof(final SequenceLocation<T> sequence) {
        return IntVariable.make(new BindingExpression() {
            public void compute() {
                pushValue(Sequences.size(sequence.get()));
            }
        }, sequence);
    }

    public static<T> IntLocation sizeof(final ObjectLocation<T> item) {
        return IntVariable.make(new BindingExpression() {
            public void compute() {
                pushValue(item.get() == null ? 0 : 1);
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

    
    public static SequenceLocation<Float> range(FloatLocation a, FloatLocation b) {
        return new BoundNumberRangeSequence(a, b);
    }
    
    public static SequenceLocation<Float> range(FloatLocation a, FloatLocation b, FloatLocation step) {
        return new BoundNumberRangeSequence(a, b, step);
    }

    public static SequenceLocation<Float> range(FloatLocation a, FloatLocation b, boolean exclusive) {
        return new BoundNumberRangeSequence(a, b, exclusive);
    }

    public static SequenceLocation<Float> range(FloatLocation a, FloatLocation b, FloatLocation step, boolean exclusive) {
        return new BoundNumberRangeSequence(a, b, step, exclusive);
    }
    
    public static<T> SequenceLocation<T> slice(TypeInfo<T> typeInfo, SequenceLocation<T> sequence, IntLocation a, IntLocation b) {
        return new BoundSequenceSlice<T>(typeInfo, sequence, a, b, false);
    }
    
    public static<T> SequenceLocation<T> sliceExclusive(TypeInfo<T> typeInfo, SequenceLocation<T> sequence, IntLocation a, IntLocation b) {
        return new BoundSequenceSlice<T>(typeInfo, sequence, a, b, true);
    }

    /** Convert any numeric sequence location to any other numeric sequence */
    public static<T extends Number, V extends Number>
    SequenceLocation<T> convertNumberSequence(final NumericTypeInfo<T> toType, final NumericTypeInfo<V> fromType, SequenceLocation<V> seq) {
        return new BoundNumericConversion<T, V>(toType, fromType, seq);
    }

    public interface SimpleBoundComprehensionCallback<T, V> {
         V computeElement$(T element, int index);
    }

    public static<T, V> SequenceLocation<V> makeSimpleBoundComprehension(TypeInfo<V> typeInfo,
                                                                         SequenceLocation<T> seq,
                                                                         boolean useIndex,
                                                                         final SimpleBoundComprehensionCallback<T, V> callback) {
        return new SimpleBoundComprehension<T, V>(typeInfo, seq, useIndex) {
            protected V computeElement$(T element, int index) {
                return callback.computeElement$(element, index);
            }
        };
    }

    public interface BoundComprehensionCallback<T_IN, T_OUT, T_IN_LOCATION extends ObjectLocation<T_IN>> {
         SequenceLocation<T_OUT> computeElements$(T_IN_LOCATION elementLocation, IntLocation indexLocation);
    }

    public static<T_IN, T_OUT, T_IN_LOCATION extends ObjectLocation<T_IN>> SequenceLocation<T_OUT>
    makeBoundComprehension(final TypeInfo<T_OUT> outTypeInfo,
                           final TypeInfo<T_IN> inTypeInfo,
                           SequenceLocation<T_IN> inSequence,
                           boolean useIndex,
                           final BoundComprehensionCallback<T_IN, T_OUT, T_IN_LOCATION> callback) {
        return new AbstractBoundComprehension<T_IN, T_IN_LOCATION, T_OUT>(outTypeInfo, inTypeInfo, inSequence, useIndex) {
            protected SequenceLocation<T_OUT> computeElements$(T_IN_LOCATION elementLocation, IntLocation indexLocation) {
                return callback.computeElements$(elementLocation, indexLocation);
            }
        };
    }
}
