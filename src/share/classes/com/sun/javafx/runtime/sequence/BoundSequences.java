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

import com.sun.javafx.runtime.location.IntLocation;
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.location.SequenceLocation;

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
    public static<T> SequenceLocation<T> singleton(Class<T> clazz, ObjectLocation<T> location) {
        return new BoundSingletonSequence<T>(clazz, location);
    }

    /** Construct a bound sequence of the form
     *   bind [ x ]
     * where x is an Integer instance.
     */
    public static<T> SequenceLocation<Integer> singleton(IntLocation location) {
        return new BoundSingletonSequence<Integer>(Integer.class, location);
    }

    public static<T> SequenceLocation<T> empty(Class<T> clazz) {
        return new AbstractBoundSequence<T>(clazz) {
            protected Sequence<T> computeValue() {
                return Sequences.emptySequence(clazz);
            }

            protected void initialize() { }
        };
    }
}
