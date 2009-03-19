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
    public static <T> SequenceLocation<T> concatenate(boolean lazy, TypeInfo<T, ?> typeInfo, SequenceLocation<? extends T>... locations) {
        return new BoundCompositeSequence<T>(lazy, typeInfo, locations);
    }

    /**
     * Construct a bound sequence of the form
     *   bind [ a, b, ... ]
     * where a, b, ..., are sequence locations.
     *
     */
    public static <T> SequenceLocation<T> concatenate(boolean lazy, TypeInfo<T, ?> typeInfo, SequenceLocation<? extends T>[] locations, int size) {
        return new BoundCompositeSequence<T>(lazy, typeInfo, locations, size);
    }

    /**
     * Construct a bound sequence of the form
     *   bind a
     * where a is a sequence location of a subtype of the desired element type
     *
     */
    public static <T, V extends T> SequenceLocation<T> upcast(boolean lazy, TypeInfo<T, ?> typeInfo, SequenceLocation<V> location) {
        return new BoundUpcastSequence<T, V>(typeInfo, location);
    }

    /** Construct a bound sequence of the form
     *   bind reverse x
     * where x is a sequence.
     */
    public static<T> SequenceLocation<T> reverse(boolean lazy, SequenceLocation<T> sequence) {
        return new BoundReverseSequence<T>(lazy, sequence);
    }

    /** Construct a bound sequence of the form
     *   bind [ x ]
     * where x is an instance.
     */
    public static<T, V extends T> SequenceLocation<T> singleton(boolean lazy, TypeInfo<T, ?> typeInfo, ObjectLocation<V> location) {
        return new BoundSingletonSequence<T, V>(typeInfo, location);
    }

    /** Construct a bound sequence of the form
     *   bind [ x ]
     * where x is an Integer instance.
     */
//    public static<T> SequenceLocation<Integer> singleton(IntLocation location) {
//        return new BoundSingletonSequence<Integer, Integer>(TypeInfo.Integer, location);
//    }

    //TODO: this seems absurd.  Why do we need it?  Why not use Sequences.empty() ?
    public static<T> SequenceLocation<T> empty(boolean lazy, final TypeInfo<T, ?> typeInfo) {
        return new AbstractBoundSequence<T>(typeInfo) {
            { setInitialValue(typeInfo.emptySequence); }
        };
    }

    public static<T> ObjectLocation<T> element(boolean lazy, SequenceLocation<T> sequence, IntLocation index) {
        return new BoundSequenceElement<T>(lazy, sequence, index);
    }

    public static IntLocation element(boolean lazy, SequenceLocation<Integer> sequence, IntLocation index) {
        return Locations.asIntLocation(new BoundSequenceElement<Integer>(lazy, sequence, index));
    }

    public static FloatLocation element(boolean lazy, SequenceLocation<Float> sequence, IntLocation index) {
        return Locations.asFloatLocation(new BoundSequenceElement<Float>(lazy, sequence, index));
    }

    public static BooleanLocation element(boolean lazy, SequenceLocation<Boolean> sequence, IntLocation index) {
        return Locations.asBooleanLocation(new BoundSequenceElement<Boolean>(lazy, sequence, index));
    }

    public static<T> IntLocation sizeof(boolean lazy, final SequenceLocation<T> sequence) {
        return IntVariable.make(new BindingExpression() {
            public void compute() {
                pushValue(Sequences.size(sequence.get()));
            }
        }, sequence);
    }

    public static<T> IntLocation sizeof(boolean lazy, final ObjectLocation<T> item) {
        return IntVariable.make(new BindingExpression() {
            public void compute() {
                pushValue(item.get() == null ? 0 : 1);
            }
        }, item);
    }

    public static SequenceLocation<Integer> range(boolean lazy, IntLocation a, IntLocation b) {
        return new BoundIntRangeSequence(lazy, a, b);
    }
    
    public static SequenceLocation<Integer> range(boolean lazy, IntLocation a, IntLocation b, IntLocation step) {
        return new BoundIntRangeSequence(lazy, a, b, step);
    }

    public static SequenceLocation<Integer> range(boolean lazy, IntLocation a, IntLocation b, boolean exclusive) {
        return new BoundIntRangeSequence(lazy, a, b, exclusive);
    }

    public static SequenceLocation<Integer> range(boolean lazy, IntLocation a, IntLocation b, IntLocation step, boolean exclusive) {
        return new BoundIntRangeSequence(lazy, a, b, step, exclusive);
    }

    
    public static SequenceLocation<Float> range(boolean lazy, FloatLocation a, FloatLocation b) {
        return new BoundNumberRangeSequence(lazy, a, b);
    }
    
    public static SequenceLocation<Float> range(boolean lazy, FloatLocation a, FloatLocation b, FloatLocation step) {
        return new BoundNumberRangeSequence(lazy, a, b, step);
    }

    public static SequenceLocation<Float> range(boolean lazy, FloatLocation a, FloatLocation b, boolean exclusive) {
        return new BoundNumberRangeSequence(lazy, a, b, exclusive);
    }

    public static SequenceLocation<Float> range(boolean lazy, FloatLocation a, FloatLocation b, FloatLocation step, boolean exclusive) {
        return new BoundNumberRangeSequence(lazy, a, b, step, exclusive);
    }
    
    public static<T> SequenceLocation<T> slice(boolean lazy, TypeInfo<T, ?> typeInfo, SequenceLocation<T> sequence, IntLocation a, IntLocation b) {
        return new BoundSequenceSlice<T>(lazy, typeInfo, sequence, a, b, false);
    }
    
    public static<T> SequenceLocation<T> sliceExclusive(boolean lazy, TypeInfo<T, ?> typeInfo, SequenceLocation<T> sequence, IntLocation a, IntLocation b) {
        return new BoundSequenceSlice<T>(lazy, typeInfo, sequence, a, b, true);
    }

    /** Convert any numeric sequence location to any other numeric sequence */
    public static<T extends Number, V extends Number>
    SequenceLocation<T> convertNumberSequence(boolean lazy, final NumericTypeInfo<T, ?> toType, final NumericTypeInfo<V, ?> fromType, SequenceLocation<V> seq) {
        return new BoundNumericConversion<T, V>(toType, fromType, seq);
    }

    public interface SimpleBoundComprehensionCallback<T, V> {
         V computeElement$(T element, int index);
    }

    public static<T, V> SequenceLocation<V> makeSimpleBoundComprehension(boolean lazy, TypeInfo<V, ?> typeInfo,
                                                                         SequenceLocation<T> seq,
                                                                         boolean useIndex,
                                                                         final SimpleBoundComprehensionCallback<T, V> callback) {
        return new SimpleBoundComprehension<T, V>(lazy, typeInfo, seq, useIndex) {
            protected V computeElement$(T element, int index) {
                return callback.computeElement$(element, index);
            }
        };
    }

    public interface BoundComprehensionCallback<T_IN, T_OUT, T_IN_LOCATION extends ObjectLocation<T_IN>> {
         SequenceLocation<T_OUT> computeElements$(T_IN_LOCATION elementLocation, IntLocation indexLocation);
    }

    public static<T_IN, T_OUT, T_IN_LOCATION extends ObjectLocation<T_IN>> SequenceLocation<T_OUT>
    makeBoundComprehension(boolean lazy, final TypeInfo<T_OUT, ?> outTypeInfo,
                           final TypeInfo<T_IN, T_IN_LOCATION> inTypeInfo,
                           SequenceLocation<T_IN> inSequence,
                           boolean useIndex,
                           final BoundComprehensionCallback<T_IN, T_OUT, T_IN_LOCATION> callback) {
        return new AbstractBoundComprehension<T_IN, T_IN_LOCATION, T_OUT>(lazy, outTypeInfo, inTypeInfo, inSequence, useIndex) {
            protected SequenceLocation<T_OUT> computeElements$(T_IN_LOCATION elementLocation, IntLocation indexLocation) {
                return callback.computeElements$(elementLocation, indexLocation);
            }
        };
    }
}
